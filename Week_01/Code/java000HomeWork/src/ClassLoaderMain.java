import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassLoaderMain {
    public static void main(String[] args) throws NoSuchMethodException, ClassNotFoundException,
            IllegalAccessException, InstantiationException, InvocationTargetException {
        //这个类class的路径
        String classPath = "C:\\Users\\alexliang\\IdeaProjects\\java000HomeWork\\Hello_154\\Hello.xlass";

        MyClassLoader myClassLoader = new MyClassLoader(classPath);

        //加载Hello这个class文件
        Class<?> Hello = myClassLoader.loadClass("Hello");

        System.out.println("类加载器是:" + Hello.getClassLoader());

        //利用反射获取main方法
        Method method = Hello.getDeclaredMethod("hello");
        Object object = Hello.newInstance();
        method.invoke(object);
    }
}
