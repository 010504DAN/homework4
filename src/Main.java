import java.util.Random;

public class Main {
    public static int bossHealth = 700;
    public static int bossDamage = 60;
    public static String bossDefence;
    public static int[] heroesHealth = {500, 280, 270, 240, 200, 230, 230, 250};
    public static int[] heroesDamage = {5, 20, 15, 10, 0, 10, 0, 25};
    public static String[] heroesAttackType = {"Golem", "Physical", "Magical", "Kinetic", "Medic", "Lucky", "Witcher", "Thor"};
    public static int roundNumber;
    public static boolean bossStunned = false; // Для способности Тора
    public static boolean witcherUsedAbility = true; // Для способности Ведьмака

    public static void main(String[] args) {
        printStatistics();

        while (!isGameOver()) {
            playRound();
        }
    }

    public static void playRound() {
        roundNumber++;
        chooseBossDefence();
        if (!bossStunned) {  // Если босс не оглушен, он атакует
            bossAttacks();
        } else {
            System.out.println("Boss is stunned and skips the round!");
            bossStunned = false;
        }
        heroesAttack();
        medicHeals();
        printStatistics();
    }

    public static boolean isGameOver() {
        if (bossHealth <= 0) {
            System.out.println("Heroes won!!!");
            return true;
        }
        boolean allHeroesDead = true;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("Boss won!!!");
            return true;
        }
        return false;
    }

    public static void chooseBossDefence() {
        Random random = new Random();
        int randomInd = random.nextInt(3); // 0,1,2 (только герои, которые наносят урон)
        bossDefence = heroesAttackType[randomInd];
    }

    public static void heroesAttack() {
        Random random = new Random();
        for (int i = 0; i < heroesDamage.length; i++) {
            if (heroesHealth[i] > 0 && bossHealth > 0) {
                int damage = heroesDamage[i];
                if (heroesAttackType[i] == "Thor") {
                    if (random.nextBoolean()) {
                        bossStunned = true;
                        System.out.println("Thor stunned the boss!");
                    }
                }
                if (heroesAttackType[i] == bossDefence) {
                    int coefficient = random.nextInt(10);
                    int criticalDamage = heroesDamage[i] * coefficient;
                    damage *= coefficient;
                    System.out.println("Critical damage by " + heroesAttackType[i] + ": " + criticalDamage);
                }
                if (bossHealth - damage < 0) {
                    bossHealth = 0;
                }else if (heroesAttackType[i] != "Medic" && heroesAttackType[i] != "Witcher") {
                    bossHealth = bossHealth - damage;
                }
            }
        }
    }

    public static void bossAttacks() {
        Random random = new Random();
        int damage = bossDamage;

        if (heroesHealth[0] != 0) {
            for (int i = 1; i < heroesHealth.length; i++) {
                if (heroesHealth[0] > 0) {
                    heroesHealth[0] -= bossDamage / 5;
                }
                else {
                    heroesHealth[0] = 0;
                    System.out.println("Golem died!");
                }
            }
            System.out.println("Golem took part of the damage!");
        }

        if (heroesHealth[0] != 0) {
            damage = bossDamage - bossDamage / 5; // Гоулем принимает на себя 20% урона

            for (int i = 0; i < heroesHealth.length; i++) {
                if (heroesHealth[i] > 0  && heroesAttackType[i] != "Witcher") {
                    if (heroesAttackType[i] == "Lucky" && random.nextBoolean()) {
                        System.out.println("Lucky dodged the attack!");
                        continue; // Lucky уклонился, пропускаем урон
                    }

                    if (heroesHealth[i] - bossDamage < 0) {
                        if (witcherUsedAbility){
                            heroesHealth[i] = heroesHealth[6];
                            heroesHealth[6] = 0;
                            witcherUsedAbility = false;
                            System.out.println("Witcher gave up her life " + heroesAttackType[i]);
                        }else {
                            heroesHealth[i] = 0;
                        }
                    } else {
                        heroesHealth[i] = heroesHealth[i] - damage;
                    }
                }
            }
        }else {
            for (int i = 0; i < heroesHealth.length; i++) {
                if (heroesHealth[i] > 0  && heroesAttackType[i] != "Witcher") {
                    if (heroesAttackType[i] == "Lucky" && random.nextBoolean()) {
                        System.out.println("Lucky dodged the attack!");
                        continue; // Lucky уклонился, пропускаем урон
                    }

                    if (heroesHealth[i] - bossDamage < 0) {
                        if (witcherUsedAbility){
                            heroesHealth[i] = heroesHealth[6];
                            heroesHealth[6] = 0;
                            witcherUsedAbility = false;
                            System.out.println("Witcher gave up her life " + heroesAttackType[i]);
                        }else {
                            heroesHealth[i] = 0;
                        }
                    } else {
                        heroesHealth[i] = heroesHealth[i] - damage;
                    }
                }
            }
        }

    }

    public static void medicHeals() {
        if (heroesHealth[3] <= 0) {
            return; // Медик мертв, не лечит
        }
        int healAmount = 30;
        int lowestHealthIndex = -1;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (i != 3 && heroesHealth[i] > 0 && heroesHealth[i] < 100) { // Медик не лечит себя и мертвых
                if (lowestHealthIndex == -1 || heroesHealth[i] < heroesHealth[lowestHealthIndex]) {
                    lowestHealthIndex = i;
                }
            }
        }
        if (lowestHealthIndex != -1) {
            heroesHealth[lowestHealthIndex] += healAmount;
            System.out.println("Medic healed " + heroesAttackType[lowestHealthIndex] + " for " + healAmount + " HP");
        }
    }

    public static void printStatistics() {
        System.out.println("ROUND " + roundNumber + " ------------");
        System.out.println("BOSS health: " + bossHealth + " damage: " + bossDamage +
                " defence: " + (bossDefence == null ? "No Defence" : bossDefence));
        for (int i = 0; i < heroesHealth.length; i++) {
            System.out.println(heroesAttackType[i] + " health: " + heroesHealth[i]
                    + " damage: " + heroesDamage[i]);
        }
        System.out.println("*******************************");
    }
}
