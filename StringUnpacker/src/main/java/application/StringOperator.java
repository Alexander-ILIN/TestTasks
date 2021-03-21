package application;

import java.util.ArrayList;
import java.util.List;

public class StringOperator
{
    //регулярное выражение - все элементы, не являющиеся цифрами; для получения массива чисел, содержащихся в исходной строке
    private static final String NOT_DIGITS = "\\D";
    //правая скобка; для получения списка индексов правой скобки в исходной строке
    private static final char RIGHT_BRACKET = ']';
    //сообщение при возникновении исключения
    public static final String EMPTY_STRING_MSG = "Введена пустая строка";
    //сообщение при возникновении исключения
    public static final String BKTS_QTY_MSG = "Нет соответствия между количествами левых и правых скобок";
    //сообщение при возникновении исключения
    public static final String BKTS_CORR_MSG = "Нет соответствия между левыми и правыми скобками";

    //валидация введённой строки
    public static List<Integer[][]> checkInput (String inputString) throws Exception
    {
        //проверка на пустую строку
        if(inputString.equals(""))
        {
            throw new Exception(EMPTY_STRING_MSG);
        }

        //получение списка позиций чисел и левых скобок
        List<Integer[][]> numberPositions = getNumberPositions (inputString);
        //получение списка позиций правых скобок
        List<Integer> bracketPositions = getBracketPositions(inputString);

        //проверка соответстия количеств левых и правых скобок
        if(numberPositions.size() != bracketPositions.size())
        {
            throw new Exception(BKTS_QTY_MSG);
        }

        //определение соответствия между левыми и правыми скобками
        List<Integer[][]> substringPositions = getSubstringPositions (numberPositions, bracketPositions);

        return substringPositions;
    }

    //распаковка ввдённой строки
    public static String unpackString (List<Integer[][]> substringPositions, String stringToUnpack)
    {
        //количество подстрок, которые необходимо распаковыть
        int replacementsQty = substringPositions.size();
        //массив, содержащий подстроки, которые необходимо распаковать и значения распакованных подстрок
        String [][] replacements = new String[replacementsQty][2];

        //массив, содержащий строки, полученные в результате каждой итерации распаковки
        String[] result = new String[replacementsQty + 1];
        //0-й элемент массива - исходная строка
        result[0] = stringToUnpack;

        //цикл распаковки подстрок
        for (int i = 0; i < replacementsQty; i++)
        {
            StringBuilder replacement = new StringBuilder();

            //начало и конец распаковываемой подстроки
            Integer[][] substringPosition = substringPositions.get(i);
            //значение распаковываемой подстроки вместе с числом и скобками
            String stringToReplace = stringToUnpack.substring(substringPosition[0][2], substringPosition[0][3] + 1);

            //значение распаковываемой подстроки без числа и скобок
            String substring = stringToUnpack.substring(substringPosition[0][1] + substringPosition[0][2] + 1, substringPosition[0][3]);

            //цикл, создающий распакованную подстроку в соответствии с количестом повторений
            for (int j = 0; j < substringPosition[0][0]; j++)
            {
                replacement.append(substring);
            }

            //значение замещаемой (нераспакованной подстроки)
            replacements[i][0] = stringToReplace;
            //значение замещающей (распакованной подстроки)
            replacements[i][1] = replacement.toString();
        }

        //цикл замены нераспакованных подстрок на распакованные
        for (int i = 1; i < replacementsQty + 1; i++)
        {
            result[i] = result[i-1].replace(replacements[i-1][0], replacements[i-1][1]);
        }

        //результат распаковки - строка
        return result[replacementsQty];
    }

    //получение позиций чисел
    private static List<Integer[][]> getNumberPositions (String string)
    {
        //получение массива, состоящего из чисел, найденных в исходной строке
        String [] numbersArray = string.split(NOT_DIGITS);

        //список, содержащий информацию о каждом числе, найденном  в исходной строке. Информация о каждом числе хранится отдельном массиве
        List<Integer[][]> numbersWithPositions = new ArrayList<>();
        int searchFrom = 0;

        //цикл, заполняющий список, содержащий информацию о каждом числе
        for(int i = 0; i < numbersArray.length; i++)
        {
            //избавление от пустых элементов
            if(!numbersArray[i].equals(""))
            {
                //массив 1х4, содержащий информацию о конкретном числе
                Integer[][] currentNumber = new Integer[1][4];
                //число, содержащееся в строке
                currentNumber[0][0] = Integer.parseInt(numbersArray[i]);
                //количество знаков в числе
                currentNumber[0][1] = numbersArray[i].length();
                //индекс первой цифры числа в исходной строке
                currentNumber[0][2] = string.indexOf(numbersArray[i], searchFrom);
                //индекс соответствующей правой скобки в исходной строке; заполняется в методе "getSubstringPositions"
                currentNumber[0][3] = 0;
                //добавление массива в список
                numbersWithPositions.add(currentNumber);
                //поиск следующего числа в исходной строке
                searchFrom = currentNumber[0][1] + currentNumber[0][2];
            }
        }

        //результат - список массивов целых чисел
        return numbersWithPositions;
    }

    //получение позиций правых скобок
    private static List<Integer> getBracketPositions (String string)
    {
        //список, содержащий индексы правых скобок в исходной строке
        List<Integer> bracketPositions = new ArrayList<>();

        // длина исходной строки
        int stringLength = string.length();

        int searchFrom = 0;

        //цикл поиска всех правых скобок
        while (searchFrom < stringLength)
        {
            Integer currentPosition = string.indexOf(RIGHT_BRACKET,searchFrom);

            //проверка, найдена ли следующая правая скобка
            if(currentPosition != -1)
            {
                searchFrom = currentPosition + 1;
                bracketPositions.add(currentPosition);
            }
            else
            {
                //создание условия выхода из цикла
                searchFrom = stringLength;
            }
        }

        //результат - список целых чисел
        return bracketPositions;
    }

    //определение соотвествия между числами / левыми скобками и правыми скобками
    private static List<Integer[][]> getSubstringPositions (List<Integer[][]> numberPositions, List<Integer> bracketPositions) throws Exception
    {
        //цикл поиска индексов правых скобок, соответствующих числам / левым скобкам
        for (int i = 0; i < numberPositions.size(); i++)
        {
            //начальный элемент в списке правых скобок
            int bracketPositionShift = 0;
            //i+1 -й элемент в списке чисел
            int nextNumberPosition = i + 1;

            //поиск правой скобки, соответствующей i-му числу / левой скобке
            //проверка окончания списков правых скобок и чисел / левых скобок
            //условие перехода к следующей правой скобке: индекс правой скобки больше индекса чисел / левых скобок, следующих за i-м числом
            while (bracketPositionShift < bracketPositions.size() &&
                    nextNumberPosition < numberPositions.size() &&
                    bracketPositions.get(bracketPositionShift) > numberPositions.get(nextNumberPosition)[0][2])
            {
                //переход к следующей правой скобке
                ++bracketPositionShift;
                //переход к следующему числу / левой скобке
                ++nextNumberPosition;
            }
            //нужная правая скобка найдена, если её индкес меньше индекса одного из чисел / левой скобки, следующего за i-м числом / левой скобкой
            Integer[][] currentNumberPosition = numberPositions.get(i);
            Integer currentBracketPosition = bracketPositions.get(bracketPositionShift);

            //проверка, был ли уже заполнен индекс правой скобки в массиве, содержащем информацию о числе / левой скобке
            //проверка на нахождение правой скобки левее числа / левой скобки
            if(currentNumberPosition[0][3] == 0 && currentBracketPosition > currentNumberPosition[0][2])
            {
                //добавление индекса правой скобки в массив, содержащий информацию о числе / левой скобке
                currentNumberPosition[0][3] = bracketPositions.get(bracketPositionShift);
                //удаление индекса обработанной правой скобки из списка индексов правых скобок
                bracketPositions.remove(currentBracketPosition);
            }
            else
            {
                //исключение, позникающее при нахождении правой скобки левее числа / левой скобки
                throw new Exception(BKTS_CORR_MSG);
            }
        }

        //резулат - список массивов целых чисел, содержащих инфомацию о подстроках в элементах:
        //0,0 - число повторений подстроки
        //0,1 - количество цифр в числе повторений подстроки
        //0,2 - индекс первой цифры числа повторений подстроки в исходной строке
        //0,3 - индекс правой скобки в исходной строке
        return numberPositions;
    }
}