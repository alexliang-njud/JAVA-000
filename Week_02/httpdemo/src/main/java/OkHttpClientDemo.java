public class OkHttpClientDemo {
    public static void main(String[] args) {
        try {
            System.out.println(OkHttpUtil.doGet("http://www.baidu.com",""));
        } catch (HttpStatusException e) {
            e.printStackTrace();
        }
    }
}
