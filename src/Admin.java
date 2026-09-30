import java.util.List;
import java.util.Scanner;

public class Admin {
    private List<Category> categories;

    private String adminId = "seongje";
    private String adminPw = "0728";

    public Admin(List<Category> categories) {
        this.categories = categories;
    }

    public void runAdminMode(Scanner scanner) {
        System.out.println("\n[ 관리자 인증이 필요합니다 ]");
        System.out.print("관리자 아이디를 입력하세요: ");
        String inputId = scanner.nextLine();
        System.out.print("관리자 비밀번호를 입력하세요: ");
        String inputPw = scanner.nextLine();

        // .equals로 투박하게 비교
        if (adminId.equals(inputId) && adminPw.equals(inputPw)) {
            System.out.println("\n[인증 성공] 관리자 시스템에 로그인되었습니다.");
        } else {
            System.out.println("[인증 실패] 아이디 또는 비밀번호가 일치하지 않습니다.");
            return;
        }

        while (true) {
            System.out.println("\n[  관리자 메뉴 선택  ]");
            System.out.println("1. 상품 추가 기능");
            System.out.println("2. 상품 수정 기능");
            System.out.println("3. 상품 삭제 기능");
            System.out.println("0. 관리자 모드 나가기");
            System.out.print("메뉴 선택 <- // ");
            int menuChoice = Integer.parseInt(scanner.nextLine());

            if (menuChoice == 0) {
                System.out.println("관리자 시스템을 종료하고 메인으로 돌아갑니다.");
                break;
            }

            System.out.println("\n[ 카테고리 선택 ]");
            for (int i = 0; i < categories.size(); i++) {
                System.out.println((i + 1) + ". " + categories.get(i).getName());
            }
            System.out.println("0. 뒤로가기");
            System.out.print("카테고리 번호 선택 <- // ");
            int catChoice = Integer.parseInt(scanner.nextLine());

            if (catChoice == 0) {
                continue;
            }

            Category targetCategory = categories.get(catChoice - 1);
            List<Product> products = targetCategory.getProducts();

            if (menuChoice == 1) {
                System.out.println("\n[ " + targetCategory.getName() + " - 상품 추가 ]");
                System.out.print("추가할 상품명: ");
                String name = scanner.nextLine();

                boolean isDuplicate = false;
                for (int i = 0; i < products.size(); i++) {
                    if (products.get(i).getName().equals(name)) {
                        isDuplicate = true;
                        break;
                    }
                }

                if (isDuplicate == true) {
                    System.out.println("[오류] 동일한 이름의 상품이 이미 해당 카테고리에 존재합니다.");
                    continue;
                }

                System.out.print("상품 가격: ");
                int price = Integer.parseInt(scanner.nextLine());
                System.out.print("상품 설명: ");
                String explanation = scanner.nextLine();
                System.out.print("재고 수량: ");
                int stock = Integer.parseInt(scanner.nextLine());

                Product newProduct = new Product(name, price, explanation, stock);
                products.add(newProduct);
                System.out.println("\n[반영 완료] '" + name + "' 상품이 성공적으로 추가되었습니다.");
            }
            else if (menuChoice == 2) {
                System.out.println("\n[ " + targetCategory.getName() + " - 상품 수정 ]");
                System.out.print("수정할 상품의 이름을 입력하세요: ");
                String searchName = scanner.nextLine();

                Product targetProduct = null;
                for (int i = 0; i < products.size(); i++) {
                    if (products.get(i).getName().equals(searchName)) {
                        targetProduct = products.get(i);
                        break;
                    }
                }

                if (targetProduct == null) {
                    System.out.println("[오류] 해당 이름의 상품을 찾을 수 없습니다.");
                    continue;
                }

                System.out.println("어떤 항목을 수정하시겠습니까?");
                System.out.println("1. 가격 수정");
                System.out.println("2. 설명 수정");
                System.out.println("3. 재고수량 수정");
                System.out.print("항목 선택 <- // ");
                int itemChoice = Integer.parseInt(scanner.nextLine());

                if (itemChoice == 1) {
                    System.out.print("새로운 가격 입력: ");
                    int newPrice = Integer.parseInt(scanner.nextLine());
                    targetProduct.setPrice(newPrice);
                } else if (itemChoice == 2) {
                    System.out.print("새로운 설명 입력: ");
                    String newExplanation = scanner.nextLine();
                    targetProduct.setExplanation(newExplanation);
                } else if (itemChoice == 3) {
                    System.out.print("새로운 재고수량 입력: ");
                    int newStock = Integer.parseInt(scanner.nextLine());
                    targetProduct.setStock(newStock);
                }
                System.out.println("[변경 완료] 상품 정보가 업데이트되었습니다.");
            }

            else if (menuChoice == 3) {
                if (products.size() == 0) {
                    System.out.println("\n[안내] 현재 이 카테고리에 등록된 상품이 없습니다.");
                    continue;
                }

                System.out.println("\n[ " + targetCategory.getName() + " 등록 상품 리스트 ]");
                for (int i = 0; i < products.size(); i++) {
                    Product p = products.get(i);
                    System.out.println((i + 1) + ". " + p.getName() + " | " + p.getPrice() + "원");
                }
                System.out.print("시스템에서 완전히 제거할 상품 번호를 입력하세요: ");
                int deleteChoice = Integer.parseInt(scanner.nextLine());

                if (deleteChoice > 0 && deleteChoice <= products.size()) {
                    Product removedProduct = products.remove(deleteChoice - 1);
                    System.out.println("\n[시스템 반영] '" + removedProduct.getName() + "' 상품이 삭제되었습니다.");
                } else {
                    System.out.println("[오류] 잘못된 번호입니다.");
                }
            }
        }
    }
}
