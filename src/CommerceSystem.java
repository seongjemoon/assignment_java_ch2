import java.util.List;
import java.util.Scanner;

public class CommerceSystem {
    private List<Category> categories;
    private Customer customer;

    public CommerceSystem(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("[ 실시간 커머스 플랫폼 메인 ]");
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i).getName());
            }
            System.out.println("0. 종료 | 프로그램 종료");
            System.out.print("0 <- // ");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 0) {
                System.out.println("커머스 플랫폼을 종료합니다.");
                break;
            }

            Category category = categories.get(choice - 1);
            showProducts(scanner, category);
        }
    }

    private void showProducts(Scanner scanner, Category category) {
        List<Product> products = category.getProducts();

        System.out.println();
        System.out.println("[ " + category.getName() + " 카테고리 ]");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.printf("%d. %-14s | %,d원 | %s%n",
                    i + 1, product.getName(), product.getPrice(), product.getExplanation());
        }
        System.out.println("0. 뒤로가기");
        System.out.print("0 <- // ");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice == 0) {
            System.out.println();
            return;
        }

        Product selected = products.get(choice - 1);
        System.out.printf("%n선택한 상품: %s | %,d원 | %s | 재고: %d개%n%n",
                selected.getName(), selected.getPrice(), selected.getExplanation(), selected.getStock());
    }
}