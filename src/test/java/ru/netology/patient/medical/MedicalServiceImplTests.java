package ru.netology.patient.medical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;

public class MedicalServiceImplTests {

    static PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);

    static final String EXPECTED_MESSAGE = "Warning, patient with id: 456985, need help";

    @BeforeAll
    public static void before() {
        PatientInfo patientInfo = new PatientInfo(
                "456985",
                null,
                null,
                null,
                new HealthInfo(
                        new BigDecimal("36.6"),
                        new BloodPressure(120, 80)
                )
        );
        Mockito.when(patientInfoRepository.getById(Mockito.anyString()))
        .thenReturn(patientInfo);
    }

    @Test
    public void checkBloodPressureTest() {
        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        MedicalService sut = new MedicalServiceImpl(patientInfoRepository, alertService);

        sut.checkBloodPressure("255469", new BloodPressure(119, 80));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(alertService).send(argumentCaptor.capture());



        Assertions.assertEquals(EXPECTED_MESSAGE, argumentCaptor.getValue());
}

    @Test
    public void checkBloodPressureWithoutCallTest() {
        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        MedicalService sut = new MedicalServiceImpl(patientInfoRepository, alertService);

        sut.checkBloodPressure("255469", new BloodPressure(120, 80));

        Mockito.verify(alertService, Mockito.times(0)).send(Mockito.any());
    }

    @Test
    public void checkTemperatureTest() {
        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        MedicalService sut = new MedicalServiceImpl(patientInfoRepository, alertService);

        sut.checkTemperature("436873", new BigDecimal("35.0"));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(alertService).send(argumentCaptor.capture());

        Assertions.assertEquals(EXPECTED_MESSAGE, argumentCaptor.getValue());
    }

    @Test
    public void checkTemperatureWithoutCallTest() {
        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        MedicalService sut = new MedicalServiceImpl(patientInfoRepository, alertService);

        sut.checkTemperature("436873", new BigDecimal("36.6"));

        Mockito.verify(alertService, Mockito.times(0)).send(Mockito.any());
    }
}
