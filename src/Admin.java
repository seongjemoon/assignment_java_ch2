public class Admin {
    //속성
    private String adminId = "seongje";
    private String adminPw = "0728";
    //기능
    //admin id pw 검사
    public boolean authenticate(String id, String pw) {
        return adminId.equals(id) && adminPw.equals(pw);
    }
    //상품 중복 여북검사
    public boolean isDuplicateProduct(Category category, String name) {
        return category.getProducts().stream()
                .anyMatch(p -> p.getName().equals(name));
    }
    //상품 추가
    public void addProduct(Category targetCategory, Product newProduct) {
        targetCategory.getProducts().add(newProduct);
    }
    //상품 삭제
    public Product removeProduct(Category targetCategory, int index) {
        return targetCategory.getProducts().remove(index);
    }
}