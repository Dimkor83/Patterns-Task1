package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryCardTest {

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() { SelenideLogger.removeListener("allure");}

    @BeforeEach
    void setUp2() {
        open("http://localhost:9999");
    }

    @Test
    public void successfulAppointmentBooking() {
        RegistrationInfo info = DataGenerator.Registration.registrationInfo("ru");
        String meetingDate = DataGenerator.meetingDate(4);
        String meetingOtherDate = DataGenerator.meetingDate(10);
        $("[placeholder=\"Город\"]").setValue(info.getCity());// заполнить поле город
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);// очистить дату
        $("[placeholder=\"Дата встречи\"]").setValue(meetingDate);// заполнить поле дата
        $("[name = \"name\"]").setValue(info.getName());//заполнить ФИО
        $("[name = \"phone\"]").setValue(info.getPhone());//заполнить телефон
        $("[data-test-id = \"agreement\"]").click();//нажать согласие
        $x("//*[text()=\"Запланировать\"]").click();//нажать забронировать
        $(".notification__content").shouldHave(Condition.text("Встреча успешно запланирована на " + meetingDate), Duration.ofSeconds(15));
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);// очистить дату
        $("[placeholder=\"Дата встречи\"]").setValue(meetingOtherDate);// заполнить поле другой датой
        $x("//*[text()=\"Запланировать\"]").click();//нажать забронировать
        $("[data-test-id = \"replan-notification\"]").shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $x("//*[text()=\"Перепланировать\"]").click();//нажать перепланировать
        $(".notification_status_ok").shouldHave(Condition.text("Встреча успешно запланирована на " + meetingOtherDate), Duration.ofSeconds(15));
    }
}
