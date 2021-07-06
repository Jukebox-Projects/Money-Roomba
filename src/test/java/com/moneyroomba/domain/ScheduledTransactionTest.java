package com.moneyroomba.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.moneyroomba.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScheduledTransactionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScheduledTransaction.class);
        ScheduledTransaction scheduledTransaction1 = new ScheduledTransaction();
        scheduledTransaction1.setId(1L);
        ScheduledTransaction scheduledTransaction2 = new ScheduledTransaction();
        scheduledTransaction2.setId(scheduledTransaction1.getId());
        assertThat(scheduledTransaction1).isEqualTo(scheduledTransaction2);
        scheduledTransaction2.setId(2L);
        assertThat(scheduledTransaction1).isNotEqualTo(scheduledTransaction2);
        scheduledTransaction1.setId(null);
        assertThat(scheduledTransaction1).isNotEqualTo(scheduledTransaction2);
    }
}
