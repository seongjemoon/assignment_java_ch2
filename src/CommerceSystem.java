import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CommerceSystem {
    private List<Category> categories;
    private Customer customer;
    private Product product;
    private Scanner scanner = new Scanner(System.in);

    private Admin admin;

    // 장바구니 리스트
    private List<Product> cart = new ArrayList<>();

    public CommerceSystem(List<Category> categories) {
        this.categories = categories;
        this.admin = new Admin(categories); // 💡 생성자에서 함께 초기화
    }

    public void start() {
        while (true) {
            System.out.println("\n[ 실시간 커머스 플랫폼 메인 ]");
            System.out.println("1. 전자제품");
            System.out.println("2. 의류");
            System.out.println("3. 식품");
            System.out.println("4. 장바구니 확인    | 장바구니를 확인 후 주문합니다.");
            System.out.println("5. 주문 취소       | 진행중인 주문을 취소합니다.");
            System.out.println("6. 관리자 모드      | 플랫폼의 상품을 관리하고 삭제합니다.");
            System.out.println("0. 종료      | 프로그램 종료");
            System.out.print("기호 또는 번호 입력 <- // ");

            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 0) {
                System.out.println("커머스 플랫폼을 종료합니다.");
                break;
            }
            else if (choice >= 1 && choice <= 3) {
                if (choice <= categories.size()) {
                    Category category = categories.get(choice - 1);
                    showFilterMenu(category);
                } else {
                    System.out.println("[안내] 해당 카테고리가 아직 데이터로 준비되지 않았습니다.");
                }
            }
            else if (choice == 4) {
                checkCartAndOrder();
            }
            else if (choice == 5) {
                cart.clear();
                System.out.println("진행중인 주문(장바구니)이 취소되었습니다.");
            }
            else if (choice == 6) {
                admin.runAdminMode(scanner);
            }
            else {
                System.out.println("[오류] 잘못된 번호입니다. 다시 입력해 주세요. (0 ~ 6 입력 가능)");
            }
        }
    }

    private void showFilterMenu(Category category) {
        while (true) {
            System.out.println("\n[ " + category.getName() + " 카테고리 ]");
            System.out.println("1. 전체 상품 보기");
            System.out.println("2. 가격대별 필터링 (100만원 이하)");
            System.out.println("3. 가격대별 필터링 (100만원 초과)");
            System.out.println("0. 뒤로가기");
            System.out.print("입력 <- // ");
            int filterChoice = Integer.parseInt(scanner.nextLine());

            if (filterChoice == 0) return;

            List<Product> filteredList;
            if (filterChoice == 1) {
                filteredList = category.getProducts().stream().collect(Collectors.toList());
                System.out.println("\n[ 전체 상품 목록 ]");
            } else if (filterChoice == 2) {
                filteredList = category.getProducts().stream()
                        .filter(p -> p.getPrice() <= 1000000)
                        .collect(Collectors.toList());
                System.out.println("\n[ 100만원 이하 상품 목록 ]");
            } else if (filterChoice == 3) {
                filteredList = category.getProducts().stream()
                        .filter(p -> p.getPrice() > 1000000)
                        .collect(Collectors.toList());
                System.out.println("\n[ 100만원 초과 상품 목록 ]");
            } else {
                System.out.println("잘못된 숫자를 넣으셨습니다.");
                continue;
            }

            showProducts(filteredList);
            return;
        }
    }

    private void showProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("조건에 맞는 상품이 존재하지 않습니다.");
            return;
        }

        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);
            System.out.printf("%d. %-14s | %,d원 | %s | 재고: %d개%n",
                    i + 1, p.getName(), p.getPrice(), p.getExplanation(), p.getStock());
        }
        System.out.println("0. 뒤로가기");
        System.out.print("입력 <- // ");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice == 0) return;

        if (choice > 0 && choice <= products.size()) {
            Product selected = products.get(choice - 1);
            System.out.printf("선택한 상품: %s | %,d원 | %s | 재고: %d개%n%n",
                    selected.getName(), selected.getPrice(), selected.getExplanation(), selected.getStock());

            System.out.printf("\"%s | %,d원 | %s\"%n위 상품을 장바구니에 추가하시겠습니까?%n",
                    selected.getName(), selected.getPrice(), selected.getExplanation());
            System.out.println("1. 확인        2. 취소");
            System.out.print("입력 <- ");
            int addCart = Integer.parseInt(scanner.nextLine());

            if (addCart == 1) {
                cart.add(selected);
                System.out.printf("%n// 1번을 누르면 나오는 메뉴입니다.%n");
                System.out.printf("%s가 장바구니에 추가되었습니다.%n", selected.getName());
            }
        } else {
            System.out.println("[오류] 잘못된 상품 번호입니다.");
        }
    }

    private void checkCartAndOrder() {
        if (cart.isEmpty()) {
            System.out.println("장바구니가 비어 있습니다.");
            return;
        }

        System.out.println("\n// 4번을 누르면 나오는 메뉴입니다.");
        System.out.println("아래와 같이 주문 하시겠습니까?");
        System.out.println("\n[ 장바구니 내역 ]");

        int totalBeforeDiscount = 0;
        for (Product p : cart) {
            System.out.printf("%s | %,d원 | %s | 수량: 1개%n", p.getName(), p.getPrice(), p.getExplanation());
            totalBeforeDiscount += p.getPrice();
        }

        System.out.println("\n[ 총 주문 금액 ]");
        System.out.printf("%,d원%n%n", totalBeforeDiscount);
        System.out.println("1. 주문 확정      2. 메인으로 돌아가기");
        System.out.print("입력 <- ");
        int orderConfirm = Integer.parseInt(scanner.nextLine());

        if (orderConfirm == 1) {
            System.out.println("\n// 1번을 누르면 할인 정보를 제공해줍니다.");
            System.out.println("고객 등급을 입력해주세요.");
            System.out.println("1. BRONZE   :  0% 할인");
            System.out.println("2. SILVER   :  5% 할인");
            System.out.println("3. GOLD     : 10% 할인");
            System.out.println("4. PLATINUM : 15% 할인");
            System.out.print("입력 <- // ");
            int gradeChoice = Integer.parseInt(scanner.nextLine());

            Customer.TierType selectedTier;
            switch (gradeChoice) {
                case 1: selectedTier = Customer.TierType.BRONZE; break;
                case 2: selectedTier = Customer.TierType.SILVER; break;
                case 3: selectedTier = Customer.TierType.GOLD; break;
                case 4: selectedTier = Customer.TierType.PLATINUM; break;
                default:
                    System.out.println("잘못된 등급 선택입니다. 주문이 취소됩니다.");
                    return;
            }

            double discountAmount = selectedTier.calculateDiscount(totalBeforeDiscount);
            int finalPrice = totalBeforeDiscount - (int) discountAmount;

            System.out.println("\n// 3번을 누르면 나오는 메뉴입니다.");
            System.out.println("주문이 완료되었습니다!");
            System.out.printf("할인 전 금액: %,d원%n", totalBeforeDiscount);

            int percent = (gradeChoice == 1) ? 0 : (gradeChoice == 2) ? 5 : (gradeChoice == 3) ? 10 : 15;
            System.out.printf("%s 등급 할인(%d%%): -%,d원%n", selectedTier.name(), percent, (int) discountAmount);
            System.out.printf("최종 결제 금액: %,d원%n", finalPrice);

            for (Product p : cart) {
                int beforeStock = p.getStock();
                p.setStock(beforeStock - 1);
                System.out.printf("%s 재고가 %d개 → %d개로 업데이트되었습니다.%n", p.getName(), beforeStock, p.getStock());
            }
            cart.clear();
        }
    }
}
