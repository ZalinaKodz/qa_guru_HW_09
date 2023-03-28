package ru.kod3ik;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;


import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.Arguments;


import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class ImdbTest {
    @BeforeEach
    void setup() {
        Selenide.open("https://www.imdb.com/");
        Configuration.browserSize = "1920x1080";
    }

    @ValueSource(strings = {
            "Last of us",
            "Friends"
    })
    @ParameterizedTest(name = "В поиске на сайте IMDB отображается не меньше 5 результатов по запросу {0}")
    void searchResultsShouldHave5results(String testData) {
        $(".imdb-header-search__input").setValue(testData).pressEnter();
        $$(".ipc-metadata-list-summary-item__c").shouldHave(sizeGreaterThanOrEqual(5));
    }

    @CsvSource(value = {
            "the big bang theory,     TV Series, Johnny Galecki, Jim Parsons",
            "Sherlock,  TV Series Benedict Cumberbatch, Martin Freeman"
    })
    @ParameterizedTest(name = "В первом результате выдачи для {0} должен отображаться текст {1}")
    void searchResultsShouldHaveText(String testKeyWord, String expectedResult) {
        $(".imdb-header-search__input").setValue(testKeyWord).pressEnter();
        $$(".ipc-metadata-list-summary-item__c").first().shouldHave(text(expectedResult));
    }

    static Stream<Arguments> checkCategoriesOfMenu() {
        return Stream.of(
                Arguments.of("Top 250 Movies",  "IMDb Top 250 as rated by regular IMDb voters."),
                Arguments.of("Most Popular Movies", "As determined by IMDb Users")
        );
    }
    @MethodSource()
    @ParameterizedTest(name = "При переходе в категорию {0}, отображается  подкатегория {1}")
    void checkCategoriesOfMenu(String testData, String expectedTexts) {
        $(".ipc-responsive-button__text").click();
        $(".navlinkcat__listContainer").$(byText(testData)).click();
        $$(".byline").shouldHave(CollectionCondition.texts(expectedTexts));
    }
}

