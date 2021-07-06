package com.moneyroomba.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.moneyroomba.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemSettingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemSetting.class);
        SystemSetting systemSetting1 = new SystemSetting();
        systemSetting1.setId(1L);
        SystemSetting systemSetting2 = new SystemSetting();
        systemSetting2.setId(systemSetting1.getId());
        assertThat(systemSetting1).isEqualTo(systemSetting2);
        systemSetting2.setId(2L);
        assertThat(systemSetting1).isNotEqualTo(systemSetting2);
        systemSetting1.setId(null);
        assertThat(systemSetting1).isNotEqualTo(systemSetting2);
    }
}
