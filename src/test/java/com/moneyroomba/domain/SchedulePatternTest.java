package com.moneyroomba.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.moneyroomba.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SchedulePatternTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SchedulePattern.class);
        SchedulePattern schedulePattern1 = new SchedulePattern();
        schedulePattern1.setId(1L);
        SchedulePattern schedulePattern2 = new SchedulePattern();
        schedulePattern2.setId(schedulePattern1.getId());
        assertThat(schedulePattern1).isEqualTo(schedulePattern2);
        schedulePattern2.setId(2L);
        assertThat(schedulePattern1).isNotEqualTo(schedulePattern2);
        schedulePattern1.setId(null);
        assertThat(schedulePattern1).isNotEqualTo(schedulePattern2);
    }
}
