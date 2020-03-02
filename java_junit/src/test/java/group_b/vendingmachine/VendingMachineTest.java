package group_b.vendingmachine;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VendingMachineTest {

    @Nested
    class CaseFailedPurchase {

        @Test
        void case_shortage_money() {
            VendingMachine machine = new VendingMachine();
            machine.insertMoney(50);
            String item = machine.purchase(Drink.WATER);
            assertEquals("", item);
        }

        @Test
        void case_shortage_money_70yen() {
            VendingMachine machine = new VendingMachine();
            machine.insertMoney(50);
            machine.insertMoney(10);
            machine.insertMoney(10);
            String item = machine.purchase(Drink.WATER);
            assertEquals("", item);
        }

        @Test
        void case_shortage_money_after_succeeded_purchase() {
            // 150円入れて、水を買うと、50円お釣りが出る
            VendingMachine machine = new VendingMachine();
            machine.insertMoney(50);
            machine.insertMoney(50);
            machine.insertMoney(50);
            String item = machine.purchase(Drink.WATER);
            assertEquals("water", item);
            int change = machine.getChange();
            assertEquals(50, change);

            // 50円を入れても、買えない
            machine.insertMoney(50);
            item = machine.purchase(Drink.WATER);
            assertEquals("", item);
            change = machine.getChange();
            assertEquals(0, change);
        }

    }

    @Nested
    class CaseReject {
        @Test
        void case_unacceptable_type() {
            Arrays.asList(1, 5, 5000, 10000).forEach((money -> {
                VendingMachine machine = new VendingMachine();
                machine.insertMoney(money);
                int change = machine.getChange();
                assertEquals((int) money, change);
            }));
        }

        @Test
        void case_acceptable_unacceptable() {
            VendingMachine machine = new VendingMachine();
            machine.insertMoney(50);
            machine.insertMoney(50);
            machine.insertMoney(50);
            machine.insertMoney(1);
            int change = machine.getChange();
            assertEquals(1, change);
        }
    }

    @Nested
    class CaseSucceededPurchase {
        @Test
        void case_water() {
            VendingMachine machine = new VendingMachine();
            machine.insertMoney(100);
            String item = machine.purchase(Drink.WATER);
            assertEquals("water", item);
        }

        @Test
        void case_water_twice_insert_money() {
            VendingMachine machine = new VendingMachine();
            machine.insertMoney(50);
            machine.insertMoney(50);
            String item = machine.purchase(Drink.WATER);
            assertEquals("water", item);
        }

        @Test
        void case_water_change_exist() {
            VendingMachine machine = new VendingMachine();
            machine.insertMoney(50);
            machine.insertMoney(50);
            machine.insertMoney(50);
            String item = machine.purchase(Drink.WATER);
            assertEquals("water", item);
            int change = machine.getChange();
            assertEquals(50, change);
        }

        @Test
        void case_water_change_exist_70yen() {
            VendingMachine machine = new VendingMachine();
            machine.insertMoney(50);
            machine.insertMoney(50);
            machine.insertMoney(50);
            machine.insertMoney(10);
            machine.insertMoney(10);
            String item = machine.purchase(Drink.WATER);
            assertEquals("water", item);
            int change = machine.getChange();
            assertEquals(70, change);
        }

        @Test
        void case_cola() {
            VendingMachine machine = new VendingMachine();
            machine.insertMoney(100);
            machine.insertMoney(10);
            machine.insertMoney(10);
            String item = machine.purchase(Drink.COLA);
            assertEquals("cola", item);
        }

        @Test
        void case_cola_change_exist() {
            VendingMachine machine = new VendingMachine();
            machine.insertMoney(50);
            machine.insertMoney(50);
            machine.insertMoney(50);
            String item = machine.purchase(Drink.COLA);
            assertEquals("cola", item);
            int change = machine.getChange();
            assertEquals(30, change);
        }
    }

    @Test
    void case_no_purchase_no_change() {
        VendingMachine machine = new VendingMachine();
        machine.insertMoney(50);
        machine.insertMoney(50);
        machine.insertMoney(50);
        int change = machine.getChange();
        assertEquals(0, change);
    }

    @Test
    void case_no_change_before_purchase_exist_change_after_purchase() {
        VendingMachine machine = new VendingMachine();
        machine.insertMoney(50);
        machine.insertMoney(50);
        machine.insertMoney(50);
        int change = machine.getChange();
        assertEquals(0, change);
        String item = machine.purchase(Drink.COLA);
        assertEquals("cola", item);
        change = machine.getChange();
        assertEquals(30, change);
        change = machine.getChange();
        assertEquals(0, change);
    }

    @Test
    void case_change_accumulate() {
        VendingMachine machine = new VendingMachine();
        machine.insertMoney(50);
        machine.insertMoney(50);
        machine.insertMoney(50);
        String item = machine.purchase(Drink.COLA);
        assertEquals("cola", item);
        machine.insertMoney(500);
        item = machine.purchase(Drink.COLA);
        assertEquals("cola", item);
        int change = machine.getChange();
        assertEquals(410, change);
        change = machine.getChange();
        assertEquals(0, change);
    }

    @Test
    void case_empty_stock() {
        VendingMachine machine = new VendingMachine();
        machine.insertMoney(50);
        machine.insertMoney(50);
        machine.insertMoney(50);
        String item = machine.purchase(Drink.COLA);
        assertEquals("", item);
    }
}