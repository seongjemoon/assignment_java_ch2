import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CommerceSystem {
    // 💡 카테고리 데이터의 유일한 관리 주체
    private List<Category> categories;
    private Scanner scanner = new Scanner(System.in);

    // 💡 Admin은 매개변수 없이 가볍게 생성
    private Admin admin = new Admin();

    private List<Product> cart = new ArrayList<>();

    public CommerceSystem(List<Category> categories) {
        this.categories = categories;
    }

    public void start() {
        while (true) {
            System.out.println("\n[ 실시간 커머스 플랫폼 메인 ]");

            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i).getName());
            }

            int cartNum = categories.size() + 1;
            int cancelNum = categories.size() + 2;
            int adminNum = categories.size() + 3;

            System.out.println(cartNum + ". 장바구니 확인    | 장바구니를 확인 후 주문합니다.");
            System.out.println(cancelNum + ". 주문 취소       | 진행중인 주문을 취소합니다.");
            System.out.println(adminNum + ". 관리자 모드      | 플랫폼의 상품을 관리하고 삭제합니다.");
            System.out.println("0. 종료      | 프로그램 종료");
            System.out.print("기호 또는 번호 입력 <- // ");

            int choice = readIntInput();

            if (choice == 0) {
                System.out.println("커머스 플랫폼을 종료합니다.");
                break;
            } else if (choice >= 1 && choice <= categories.size()) {
                Category category = categories.get(choice - 1);
                showFilterMenu(category);
            } else if (choice == cartNum) {
                checkCartAndOrder();
            } else if (choice == cancelNum) {
                cart.clear();
                System.out.println("진행중인 주문(장바구니)이 취소되었습니다.");
            } else if (choice == adminNum) {
                runAdminMode();
            } else {
                System.out.println("[오류] 잘못된 번호입니다. 다시 입력해 주세요.");
            }
        }
    }

    private Category selectCategory(String actionTitle) {
        System.out.println("\n[ " + actionTitle + " - 카테고리 선택 ]");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).getName());
        }
        System.out.println("0. 뒤로가기");
        System.out.print("카테고리 번호 선택 <- // ");

        int catChoice = readIntInput();
        if (catChoice == 0) return null;
        if (catChoice < 1 || catChoice > categories.size()) {
            System.out.println("[오류] 존재하지 않는 카테고리 번호입니다.");
            return null;
        }
        return categories.get(catChoice - 1);
    }

    private void runAdminMode() {
        System.out.println("\n[ 관리자 인증이 필요합니다 ]");
        System.out.print("관리자 아이디를 입력하세요: ");
        String inputId = scanner.nextLine();
        System.out.print("관리자 비밀번호를 입력하세요: ");
        String inputPw = scanner.nextLine();

        if (!admin.authenticate(inputId, inputPw)) {
            System.out.println("[인증 실패] 아이디 또는 비밀번호가 일치하지 않습니다.");
            return;
        }

        System.out.println("\n[인증 성공] 관리자 시스템에 로그인되었습니다.");

        while (true) {
            System.out.println("\n[  관리자 메뉴 선택  ]");
            System.out.println("1. 상품 추가 기능");
            System.out.println("2. 상품 수정 기능");
            System.out.println("3. 상품 삭제 기능");
            System.out.println("0. 관리자 모드 나가기");
            System.out.print("메뉴 선택 <- // ");

            int menuChoice = readIntInput();
            if (menuChoice == 0) break;
            if (menuChoice < 1 || menuChoice > 3) {
                System.out.println("[오류] 올바른 메뉴 번호를 입력해 주세요.");
                continue;
            }

            String actionTitle = switch (menuChoice) {
                case 1 -> "상품 추가";
                case 2 -> "상품 수정";
                case 3 -> "상품 삭제";
                default -> "";
            };

            // 카테고리 선택
            Category targetCategory = selectCategory(actionTitle);
            if (targetCategory == null) continue;

            List<Product> products = targetCategory.getProducts();

            // 1. 상품 추가
            if (menuChoice == 1) {
                System.out.print("추가할 상품명: ");
                String name = scanner.nextLine();

                if (admin.isDuplicateProduct(targetCategory, name)) {
                    System.out.println("[오류] 동일한 이름의 상품이 이미 존재합니다.");
                    continue;
                }

                System.out.print("상품 가격: ");
                int price = readIntInput();
                System.out.print("상품 설명: ");
                String explanation = scanner.nextLine();
                System.out.print("재고 수량: ");
                int stock = readIntInput();

                Product newProduct = new Product(name, price, explanation, stock);
                admin.addProduct(targetCategory, newProduct);
                System.out.println("\n[반영 완료] '" + name + "' 상품이 성공적으로 추가되었습니다.");
            }
            // 2. 상품 수정
            else if (menuChoice == 2) {
                System.out.print("수정할 상품의 이름을 입력하세요: ");
                String searchName = scanner.nextLine();

                Product targetProduct = products.stream()
                        .filter(p -> p.getName().equals(searchName))
                        .findFirst()
                        .orElse(null);

                if (targetProduct == null) {
                    System.out.println("[오류] 해당 이름의 상품을 찾을 수 없습니다.");
                    continue;
                }

                System.out.println("어떤 항목을 수정하시겠습니까?");
                System.out.println("1. 가격 수정 | 2. 설명 수정 | 3. 재고수량 수정");
                System.out.print("항목 선택 <- // ");
                int itemChoice = readIntInput();

                switch (itemChoice) {
                    case 1 -> {
                        System.out.print("새로운 가격 입력: ");
                        targetProduct.setPrice(readIntInput());
                    }
                    case 2 -> {
                        System.out.print("새로운 설명 입력: ");
                        targetProduct.setExplanation(scanner.nextLine());
                    }
                    case 3 -> {
                        System.out.print("새로운 재고수량 입력: ");
                        targetProduct.setStock(readIntInput());
                    }
                    default -> System.out.println("[오류] 잘못된 선택입니다.");
                }
                System.out.println("[변경 완료] 상품 정보가 업데이트되었습니다.");
            }
            // 3. 상품 삭제
            else if (menuChoice == 3) {
                if (products.isEmpty()) {
                    System.out.println("\n[안내] 현재 이 카테고리에 등록된 상품이 없습니다.");
                    continue;
                }

                System.out.println("\n[ " + targetCategory.getName() + " 등록 상품 리스트 ]");
                for (int i = 0; i < products.size(); i++) {
                    Product p = products.get(i);
                    System.out.println((i + 1) + ". " + p.getName() + " | " + p.getPrice() + "원");
                }
                System.out.print("시스템에서 완전히 제거할 상품 번호를 입력하세요: ");
                int deleteChoice = readIntInput();

                if (deleteChoice > 0 && deleteChoice <= products.size()) {
                    Product removedProduct = admin.removeProduct(targetCategory, deleteChoice - 1);
                    System.out.println("\n[시스템 반영] '" + removedProduct.getName() + "' 상품이 삭제되었습니다.");
                } else {
                    System.out.println("[오류] 잘못된 번호입니다.");
                }
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

            int filterChoice = readIntInput();
            if (filterChoice == 0) return;

            List<Product> filteredList;
            if (filterChoice == 1) {
                filteredList = new ArrayList<>(category.getProducts());
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
        int choice = readIntInput();

        if (choice == 0) return;

        if (choice > 0 && choice <= products.size()) {
            Product selected = products.get(choice - 1);

            if (selected.getStock() <= 0) {
                System.out.println("[오류] 해당 상품은 품절되었습니다.");
                return;
            }

            System.out.printf("\"%s | %,d원 | %s\"%n위 상품을 장바구니에 추가하시겠습니까?%n",
                    selected.getName(), selected.getPrice(), selected.getExplanation());
            System.out.println("1. 확인        2. 취소");
            System.out.print("입력 <- ");
            int addCart = readIntInput();

            if (addCart == 1) {
                cart.add(selected);
                System.out.printf("%n%s가 장바구니에 추가되었습니다.%n", selected.getName());
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

        System.out.println("\n아래와 같이 주문 하시겠습니까?");
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
        int orderConfirm = readIntInput();

        if (orderConfirm == 1) {
            System.out.println("\n고객 등급을 입력해주세요.");
            System.out.println("1. BRONZE   :  0% 할인");
            System.out.println("2. SILVER   :  5% 할인");
            System.out.println("3. GOLD     : 10% 할인");
            System.out.println("4. PLATINUM : 15% 할인");
            System.out.print("입력 <- // ");
            int gradeChoice = readIntInput();

            Customer.TierType selectedTier = switch (gradeChoice) {
                case 1 -> Customer.TierType.BRONZE;
                case 2 -> Customer.TierType.SILVER;
                case 3 -> Customer.TierType.GOLD;
                case 4 -> Customer.TierType.PLATINUM;
                default -> null;
            };

            if (selectedTier == null) {
                System.out.println("잘못된 등급 선택입니다. 주문이 취소됩니다.");
                return;
            }

            double discountAmount = selectedTier.calculateDiscount(totalBeforeDiscount);
            int finalPrice = totalBeforeDiscount - (int) discountAmount;

            System.out.println("\n주문이 완료되었습니다!");
            System.out.printf("할인 전 금액: %,d원%n", totalBeforeDiscount);

            int percent = switch (gradeChoice) {
                case 2 -> 5;
                case 3 -> 10;
                case 4 -> 15;
                default -> 0;
            };

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

    private int readIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("[오류] 숫자만 입력 가능합니다. 다시 입력해 주세요: ");
            }
        }
    }
}