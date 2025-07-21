package server;

import framework.annotations.Controller;
import framework.annotations.GET;
import framework.annotations.POST;
import framework.annotations.Path;
import framework.response.Response;
import framework.response.RouteNotFoundResponse;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class WebFramework {

    public static List<String> classNameList = new ArrayList<>();
    private static Map<Class<?>,Object> controllerInstanceMap = new HashMap<>();
    private static Map<String,Method> requestMethodMap = new HashMap<>();

    public static void initializeFramework() throws InvocationTargetException, IllegalAccessException {

        getNamesOfClasses(new File("./src/main/java"));
        instantiateControllerClasses();
        mapRequestsAndMethods();

    }

    public static Response handleRequest(String parsedRequest, Map<String, String> parameters) throws InvocationTargetException, IllegalAccessException {

        Method method = requestMethodMap.get(parsedRequest);

        if((requestMethodMap.get(parsedRequest)) == null)
            return new RouteNotFoundResponse();

        Class<?> methodClass = method.getDeclaringClass();

        Object singleton = controllerInstanceMap.get(methodClass);

        if (method.isAnnotationPresent(POST.class))
            return (Response) method.invoke(singleton, parameters);

        return (Response) method.invoke(singleton);

    }

    private static void getNamesOfClasses(File srcDirectory) {

        File[] files = srcDirectory.listFiles();

        for(File file : files){

            if(file.isDirectory())
                getNamesOfClasses(file);

            if(file.isFile() && file.getName().endsWith(".java"))
                parseAndAddNameToList(file.getPath());

        }


    }

    private static void parseAndAddNameToList(String filePath){

        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(((filePath.replace("./src/main/java/","")).replace("/",".")));

        classNameList.add(stringBuilder.substring(0,(stringBuilder.length()-5)));

    }

    private static void instantiateControllerClasses(){

        DIEngine.setSingletonInstances();
        DIEngine.setDependencyContainer();

        for(String className : classNameList){

            try {

                Class<?> cl = Class.forName(className);

                if(cl.isAnnotationPresent(Controller.class)){

                    Object object = DIEngine.injection(cl);;
                    controllerInstanceMap.put(object.getClass(),object);

                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (DIEngine.QualifierMissingException e) {
                e.printStackTrace();
            } catch (DIEngine.QualifierNotFoundException e) {
                e.printStackTrace();
            } catch (DIEngine.AutowiredAttributeNotAnnotatedException e) {
                e.printStackTrace();
            }

        }

    }

    private static void mapRequestsAndMethods() {

        for(Class<?> cl : controllerInstanceMap.keySet()){

            Method[] methods = cl.getDeclaredMethods();

            for(Method method : methods){

                if(method.isAnnotationPresent(Path.class)){

                    Path pathAnnotation = (Path) method.getAnnotation(Path.class);
                    GET getAnnotation = (GET) method.getAnnotation(GET.class);
                    POST postAnnotation = (POST) method.getAnnotation(POST.class);

                    if((getAnnotation == null) && (postAnnotation == null))
                        continue;

                    if((getAnnotation != null) && (postAnnotation != null))
                        continue;

                    if(pathAnnotation.path().isEmpty())
                        continue;

                    StringBuilder builder = new StringBuilder("");

                    if(getAnnotation!=null)
                        builder.append("GET");
                    else
                        builder.append("POST");

                    builder.append(pathAnnotation.path());

                    requestMethodMap.put(builder.toString(),method);

                }

            }

        }

    }

}
