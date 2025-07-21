package server;

import framework.annotations.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class DIEngine {

    private static Map<Class<?>,Object> singletonInstances = new HashMap<>();
    private static Map<String,Class<?>> dependencyContainer = new HashMap<>();

    public static void setSingletonInstances(){

        for(String className : WebFramework.classNameList){

            try {

                Class<?> cl = Class.forName(className);

                if(cl.isAnnotationPresent(Bean.class) || cl.isAnnotationPresent(Service.class)){

                    Bean beanAnnotation = cl.getAnnotation(Bean.class);

                    if((beanAnnotation != null) && (!(beanAnnotation.scope().equals("singleton"))))
                        continue;

                    Object object = cl.getDeclaredConstructor().newInstance();
                    singletonInstances.put(cl,object);

                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

        }

    }

    public static void setDependencyContainer(){

        for(String className : WebFramework.classNameList){

            try {

                Class<?> cl = Class.forName(className);

                if(cl.isAnnotationPresent(Qualifier.class)){

                    Qualifier qualifierAnnotation = cl.getAnnotation(Qualifier.class);
                    String qualifierValue = qualifierAnnotation.value();

                    if(qualifierValue!="") {

                        Object object = dependencyContainer.get(qualifierValue);

                        if(object!=null)
                            throw new QualifierConflictException();

                        dependencyContainer.put(qualifierValue, cl);
                    }

                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (QualifierConflictException e) {
                e.printStackTrace();
            }

        }

    }

    private static Object initializeObject(Class<?> cl) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, AutowiredAttributeNotAnnotatedException {

        Object obj = null;

        if(cl.isAnnotationPresent(Controller.class)){
            obj = cl.getDeclaredConstructor().newInstance();
        }else if(singletonInstances.get(cl)!=null){
            obj = singletonInstances.get(cl);
        }else if(cl.isAnnotationPresent(Component.class) || cl.isAnnotationPresent(Bean.class)){

            Bean beanAnnotation = cl.getAnnotation(Bean.class);

            if((beanAnnotation!=null && beanAnnotation.scope().equals("prototype")) || cl.isAnnotationPresent(Component.class))
                obj = cl.getDeclaredConstructor().newInstance();

        }else if(!cl.isInterface()){
            throw new AutowiredAttributeNotAnnotatedException("Autowired attribute type " + cl.getName() + " not annotated with Bean, Service or Component!");
        }

        return obj;

    }

    public static Object injection(Class<?> cl) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, QualifierMissingException, QualifierNotFoundException, AutowiredAttributeNotAnnotatedException {

        Object obj = initializeObject(cl);

        Field[] fields = cl.getDeclaredFields();

        for(Field field : fields){

            if(field.isAnnotationPresent(Autowired.class)) {

                Object returnedObject = null;
                Class<?> fieldImplementation = null;

                if(field.getType().isInterface()){
                    Qualifier fieldQualifier = field.getAnnotation(Qualifier.class);

                    if(fieldQualifier==null)
                        throw new QualifierMissingException();

                    String qualifierId = fieldQualifier.value();

                    Class<?> implementationClass = dependencyContainer.get(qualifierId);

                    if(implementationClass==null)
                        throw new QualifierNotFoundException();

                    fieldImplementation = implementationClass;

                }else{
                    fieldImplementation = field.getType();
                }

                returnedObject = injection(fieldImplementation);

                field.setAccessible(true);
                field.set(obj,returnedObject);


                Autowired autowired = field.getAnnotation(Autowired.class);

                if(autowired.verbose())
                    System.out.println("Initialized " + field.get(obj).getClass().getName() + " " + field.getName() + " in " + field.getDeclaringClass().getName() + " on " + LocalDateTime.now() + " with " + field.get(obj).hashCode());

            }

        }

        return obj;

    }

    public static class QualifierMissingException extends Exception {

        public QualifierMissingException() {
            super("Interface attribute not annotated with Qualifier!");
        }

    }

    public static class QualifierNotFoundException extends Exception {

        public QualifierNotFoundException(){
            super("Qualifier not found in Dependency Container!");
        }

    }

    public static class QualifierConflictException extends Exception{

        public QualifierConflictException(){
            super("Qualifier already assigned to a different class!");
        }

    }

    public static class AutowiredAttributeNotAnnotatedException extends Exception {

        public AutowiredAttributeNotAnnotatedException(String errorMessage){
            super(errorMessage);
        }


    }


}
