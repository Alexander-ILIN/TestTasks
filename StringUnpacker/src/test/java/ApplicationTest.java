import application.Application;
import application.StringOperator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ApplicationTest
{
    private Map<String, ByteArrayInputStream> valuesForInputTest = new HashMap<>();
    private String[][] valuesForUnpackingTest = new String[7][2];
    private String emptyStringTest = "";
    private String bktsQtyTest = "2[a";
    private String bktsCorrTest = "2[a]]3[b";
    private String notValidCharsTest = "123";

    @Before
    public void dataForTest()
    {
        //данные для теста метода Application.userInput()
        //валидная строка
        valuesForInputTest.put("3[xyz]4[xy]z", new ByteArrayInputStream("3[xyz]4[xy]z".getBytes()));
        //0 - для выхода из приложения
        valuesForInputTest.put("0", new ByteArrayInputStream("0".getBytes()));
        //строка, содержащая невалидные символы
        valuesForInputTest.put(notValidCharsTest, new ByteArrayInputStream(notValidCharsTest.getBytes()));
        //невалидные строки, которые метод Application.userInput() пропускает
        valuesForInputTest.put(bktsQtyTest, new ByteArrayInputStream(bktsQtyTest.getBytes()));
        valuesForInputTest.put(bktsCorrTest, new ByteArrayInputStream(bktsCorrTest.getBytes()));
        valuesForInputTest.put(emptyStringTest, new ByteArrayInputStream(emptyStringTest.getBytes()));

        //данные для теста методов Application.launchUnpacking(), StringOperator.checkInput(), StringOperator.unpackString(), StringOperator.getNumberPositions(), StringOperator.getBracketPositions(), StringOperator.getSubstringPositions()
        //валидные строки
        valuesForUnpackingTest[0][0] = "3[xyz]4[xy]z";
        valuesForUnpackingTest[0][1] = "xyzxyzxyzxyxyxyxyz";
        valuesForUnpackingTest[1][0] = "2[3[x]y]";
        valuesForUnpackingTest[1][1] = "xxxyxxxy";
        valuesForUnpackingTest[2][0] = "abc";
        valuesForUnpackingTest[2][1] = "abc";
        valuesForUnpackingTest[3][0] = "a2[b2[c2[d]]]e3[f]g";
        valuesForUnpackingTest[3][1] = "abcddcddbcddcddefffg";
        //невалидные строки, которые метод Application.userInput() пропускает
        valuesForUnpackingTest[4][0] = emptyStringTest;
        valuesForUnpackingTest[4][1] = StringOperator.EMPTY_STRING_MSG;
        valuesForUnpackingTest[5][0] = bktsQtyTest;
        valuesForUnpackingTest[5][1] = StringOperator.BKTS_QTY_MSG;
        valuesForUnpackingTest[6][0] = bktsCorrTest;
        valuesForUnpackingTest[6][1] = StringOperator.BKTS_CORR_MSG;
    }

    //тест метода Application.userInput()
    @Test
    public void testUserInput()
    {
        Set<String> userInputs = valuesForInputTest.keySet();

        //запуск цикла проверок
        for(String userInput : userInputs)
        {
            //имитация ввода данных в консоль
            System.setIn(valuesForInputTest.get(userInput));
            try
            {
                //сравнение введённой строки и строки, полученной в результате выполнения метода
                Assert.assertEquals(userInput,Application.userInput());
            }
            catch (Exception e)
            {
                //сравнение текста исключения
                if(userInput.equals(notValidCharsTest))
                {
                    Assert.assertTrue(e.getMessage().contains(Application.NOT_VALID_CHARS_MSG));
                }
            }
        }
    }

    //тест методов Application.launchUnpacking(), StringOperator.checkInput(), StringOperator.unpackString(), StringOperator.getNumberPositions(), StringOperator.getBracketPositions(), StringOperator.getSubstringPositions()
    @Test
    public void testLaunchUnpacking()
    {
        //запуск цикла проверок
        for (int i = 0; i < valuesForUnpackingTest.length; i++)
        {
            try
            {
                String expected = valuesForUnpackingTest[i][1];
                String actual = Application.launchUnpacking(valuesForUnpackingTest[i][0]);
                Assert.assertEquals(expected, actual);
            }
            catch (Exception e)
            {
                //сравнение текста возникающих исключений
                Assert.assertTrue(e.getMessage().contains(valuesForUnpackingTest[i][1]));
            }
        }
    }
}
