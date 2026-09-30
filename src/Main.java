import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Product> electronicsList = new ArrayList<>();
        electronicsList.add(new Product("Galaxy S24", 1200000, "최신 안드로이드 스마트폰", 50));
        electronicsList.add(new Product("iPhone 15", 1350000, "Apple의 최신 스마트폰", 30));
        electronicsList.add(new Product("MacBook Pro", 2400000, "M3 칩셋이 탑재된 노트북", 20));
        electronicsList.add(new Product("AirPods Pro", 350000, "노이즈 캔슬링 무선 이어폰", 50));

        Category electronics = new Category("전자제품", electronicsList);
        Category clothing = new Category("의류", new ArrayList<>());
        Category food = new Category("식품", new ArrayList<>());

        List<Category> categories = new ArrayList<>();
        categories.add(electronics);
        categories.add(clothing);
        categories.add(food);

        CommerceSystem system = new CommerceSystem(categories);

        system.start();
    }
}
