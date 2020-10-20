import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MyClassLoader extends ClassLoader {
    //指定路径
    private String path ;

    public MyClassLoader(String classPath){
        path=classPath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException{
        Class log = null;
        // 获取该class文件字节码数组
        byte[] classData = getData();

        if (classData != null) {
            // 将class的字节码数组转换成Class类的实例
            log = defineClass(name, classData, 0, classData.length);
        }
        return log;
    }

    /**
     * 将class文件转化为字节码数组
     * @return
     */
    private byte[] getData() {
        //读入指定文件路径的文件
        File file = new File(path);
        if (file.exists()){
            FileInputStream in = null;
            ByteArrayOutputStream out = null;
            try {
                in = new FileInputStream(file);
                out = new ByteArrayOutputStream();

                int nextValue = 0;
                //字节转换 x=255-x
                while ((nextValue = in.read()) != -1) {
                    out.write(255-nextValue);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return out.toByteArray();
        }else{
            return null;
        }
    }

    public static void main(String[] args){
        ClassLoader ClassLoader1 = MyClassLoader.class.getClassLoader();
        ClassLoader ClassLoader2 = ClassLoader1.getParent();
        ClassLoader ClassLoader3 = ClassLoader2.getParent();

        System.out.println(ClassLoader1);
        System.out.println(ClassLoader2);
        System.out.println(ClassLoader3);
    }

}
