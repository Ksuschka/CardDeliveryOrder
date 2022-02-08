package ru.netology;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardDeliveryOrderTest {
    public String generateDate(int step) {
        String date;
        LocalDate localDate = LocalDate.now().plusDays(step);
        date = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(localDate);
        return date;
    }


    @BeforeEach
    public void openBrowser() {
        open("http://localhost:9999");
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "1920x1080";
    }


    @Test
    public void shouldApplicationForm() {

        String date = generateDate(3);
        $("[data-test-id='city'] .input__control").setValue("Владимир");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.BACK_SPACE, date);
        $("[data-test-id='name'] .input__control").setValue("Петров-Водкин Кузьма");
        $("[data-test-id='phone'] .input__control").setValue("+79106588785");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $(".notification_visible")
                .shouldBe(exist, Duration.ofSeconds(15));
        $("[data-test-id='notification'] .notification__title")
                .shouldHave(text("Успешно!"), Duration.ofSeconds(15));
        $("[data-test-id='notification'] .notification__content")
                .shouldHave(text("Встреча успешно забронирована на " + date), Duration.ofSeconds(15));

    }

    @Test
    public void shouldApplicationFormNoCity() {

        String date = generateDate(4);
        $("[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.BACK_SPACE, date);
        $("[data-test-id='name'] .input__control").setValue("Петров-Водкин Кузьма");
        $("[data-test-id='phone'] .input__control").setValue("+79106588785");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldApplicationFormInaccessibleCity() {

        String date = generateDate(4);
        $("[data-test-id='city'] .input__control").setValue("Рыбное");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.BACK_SPACE, date);
        $("[data-test-id='name'] .input__control").setValue("Петров-Водкин Кузьма");
        $("[data-test-id='phone'] .input__control").setValue("+79106588785");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id='city'].input_invalid .input__sub")
                .shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    public void shouldApplicationFormNoDate() {

        $("[data-test-id='city'] .input__control").setValue("Владимир");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='name'] .input__control").setValue("Петров-Водкин Кузьма");
        $("[data-test-id='phone'] .input__control").setValue("+79206548998");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $(withText("Неверно введена дата"))
                .shouldBe(Condition.visible);
    }

    @Test
    public void shouldApplicationFormPastDate() {

        $("[data-test-id='city'] .input__control").setValue("Владимир");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.BACK_SPACE);
        $("[data-test-id='date'] .input__control").setValue("00000000");
        $("[data-test-id='name'] .input__control").setValue("Петров-Водкин Кузьма");
        $("[data-test-id='phone'] .input__control").setValue("+79206548998");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $(withText("Неверно введена дата"))
                .shouldBe(Condition.visible);
    }

    @Test
    public void shouldApplicationFormWrongDate() {

        String date = generateDate(1);
        $("[data-test-id='city'] .input__control").setValue("Владимир");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.BACK_SPACE, date);
        $("[data-test-id='name'] .input__control").setValue("Петров-Водкин Кузьма");
        $("[data-test-id='phone'] .input__control").setValue("+79206548998");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $(withText("Заказ на выбранную дату невозможен"))
                .shouldBe(Condition.visible);
    }

    @Test
    public void shouldApplicationFormNoName() {

        String date = generateDate(4);
        $("[data-test-id='city'] .input__control").setValue("Владимир");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.BACK_SPACE, date);
        $("[data-test-id='phone'] .input__control").setValue("+79106588785");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldApplicationFormEnglishName() {

        String date = generateDate(4);
        $("[data-test-id='city'] .input__control").setValue("Владимир");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.BACK_SPACE, date);
        $("[data-test-id='name'] .input__control").setValue("Anna Ivanova");
        $("[data-test-id='phone'] .input__control").setValue("+79106588785");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id='name'].input_invalid .input__sub")
                .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }


    @Test
    public void shouldApplicationFormNoPhone() {

        String date = generateDate(4);
        $("[data-test-id='city'] .input__control").setValue("Владимир");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.BACK_SPACE, date);
        $("[data-test-id='name'] .input__control").setValue("Петров-Водкин Кузьма");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub")
                .shouldHave(text("Поле обязательно для заполнения"));
    }

    @Test
    public void shouldApplicationFormShortPhone() {

        String date = generateDate(4);
        $("[data-test-id='city'] .input__control").setValue("Владимир");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.BACK_SPACE, date);
        $("[data-test-id='name'] .input__control").setValue("Петров-Водкин Кузьма");
        $("[data-test-id='phone'] .input__control").setValue("+791065");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub")
                .shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    public void shouldApplicationFormLongPhone() {

        String date = generateDate(4);
        $("[data-test-id='city'] .input__control").setValue("Владимир");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.BACK_SPACE, date);
        $("[data-test-id='name'] .input__control").setValue("Петров-Водкин Кузьма");
        $("[data-test-id='phone'] .input__control").setValue("+79106500000000000");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub")
                .shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    public void shouldApplicationFormFalsePhone() {

        String date = generateDate(4);
        $("[data-test-id='city'] .input__control").setValue("Владимир");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.BACK_SPACE, date);
        $("[data-test-id='name'] .input__control").setValue("Петров-Водкин Кузьма");
        $("[data-test-id='phone'] .input__control").setValue("89206548998");
        $("[data-test-id='agreement'] .checkbox__box").click();
        $(withText("Забронировать")).click();
        $("[data-test-id='phone'].input_invalid .input__sub")
                .shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    public void shouldApplicationFormNoCheckbox() {

        String date = generateDate(4);
        $("[data-test-id='city'] .input__control").setValue("Владимир");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.CONTROL + "A");
        $("[data-test-id='date'] .input__control").sendKeys(Keys.BACK_SPACE, date);
        $("[data-test-id='name'] .input__control").setValue("Петров-Водкин Кузьма");
        $("[data-test-id='phone'] .input__control").setValue("+79106546565");
        $(withText("Забронировать")).click();
        String text = $(".checkbox__text").getCssValue("color");
        assertEquals("rgba(255, 92, 92, 1)", text);
    }

}


