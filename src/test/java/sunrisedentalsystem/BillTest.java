package sunrisedentalsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import sunrisedentalsystem.model.Bill;

class BillTest {

    @Test
    void shouldCalculateTotalBillAmount() {

        Bill bill = new Bill();

        bill.setConsultationFee(1000.00);
        bill.setTreatmentCost(5000.00);

        double result = bill.calculateTotal();

        assertEquals(6000.00, result, 0.001);
    }

    @Test
    void shouldStoreCalculatedTotalAmount() {

        Bill bill = new Bill();

        bill.setConsultationFee(1000.00);
        bill.setTreatmentCost(2500.00);

        bill.calculateTotal();

        assertEquals(3500.00, bill.getTotalAmount(), 0.001);
    }
}

