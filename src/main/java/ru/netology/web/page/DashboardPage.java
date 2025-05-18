package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.val;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class DashboardPage {
    private final SelenideElement heading = $("[data-test-id=dashboard]");
    private final ElementsCollection cards = $$(".list__item div");
    private final SelenideElement reloadButton = $("[data-test-id='action-reload']");
    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";


    public DashboardPage() {
        heading.shouldBe(visible);
    }

    private SelenideElement getCardInfo(DataHelper.CardInfo cardInfo) { //ищем карту по ID
        return cards.findBy(Condition.attribute("data-test-id", cardInfo.getTestId()));
    }

    public int getCardBalance(DataHelper.CardInfo cardInfo) { //ищем баланс, принимает объект номер карты
        var text = getCardInfo(cardInfo).getText(); //getCardInfo-выполняет поиск карты, получает текст getText
        return extractBalance(text);
    }

    public TransferPage selectCard(DataHelper.CardInfo cardInfo) {
        getCardInfo(cardInfo).$("button").click(); //getCardInfo(cardInfo) - ищет карту, далее в этом же элементе ищет кнопку
        return new TransferPage();
    }

    public void reloadDashboard() { //обновление страницы
        reloadButton.click();
        heading.shouldBe(visible);
    }

    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }
    //для проверки баланса карт
    public void checkCardBalance(DataHelper.CardInfo cardInfo, int expectedBalance) { //cardInfo - ищем карту,
        getCardInfo(cardInfo).should(visible).should(text(balanceStart + expectedBalance + balanceFinish)); //getCardInfo(cardInfo) - нашли строчку карты, should(visible) - проверили ее видимость, should(text(balanceStart + expectedBalance + balanceFinish)- проверяем текст
    }

}