public class Customer {
    // 속성
    private String name;
    private String email;
    private String tier;

    // 생성자
    @FunctionalInterface
    public interface LambdaExpression {
        double run(double price);
    }
    public enum TierType {
        BRONZE(price -> price * 0.0),
        SILVER(price -> price * 0.05),
        GOLD(price -> price * 0.10),
        PLATINUM(price -> price * 0.15);

        private final LambdaExpression expression;

        TierType(LambdaExpression expression) {
            this.expression = expression;
        }

        public double calculateDiscount(double price) {
            return this.expression.run(price);
        }
    }

    public Customer(String name, String email, String tier) {
        this.name = name;
        this.email = email;
        this.tier = tier;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTier() {
        return tier;
    }

}
