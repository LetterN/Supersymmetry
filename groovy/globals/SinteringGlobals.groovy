class SinteringGlobals {

    public static void main (String[] args) {

    }

    public static class Combustible {
        String name
        boolean isPlasma
        int amountRequired
        int duration
        String byproduct
        int byproductAmount

        Combustible(name, isPlasma, amountRequired, duration, byproduct, byproductAmount) {
            this.name = name
            this.isPlasma = isPlasma
            this.amountRequired = amountRequired
            this.duration = duration
            this.byproduct = byproduct
            this.byproductAmount = byproductAmount
        }
    }

    public static class Comburent {
        String name
        int amountRequired
        int duration

        Comburent(name, amountRequired, duration) {
            this.name = name
            this.amountRequired = amountRequired
            this.duration = duration
        }
    }

    public static class Blanket {
        String name
        int amountRequired
        int duration

        Blanket(name, amountRequired, duration) {
            this.name = name
            this.amountRequired = amountRequired
            this.duration = duration
        }
    }

    public static sintering_fuels = [
        new Combustible('methane', false, 100, 100, 'carbon_dioxide', 50),
        new Combustible('syngas', false, 100, 100, 'carbon_dioxide', 50),
        new Combustible('plasma.helium', true, 10, 10, 'helium', 10)
    ]

    public static sintering_comburents = [
        new Comburent('air', 100, 50)
    ]

    public static sintering_blankets = [
        new Blanket('argon', 100, 300)
    ]
}
