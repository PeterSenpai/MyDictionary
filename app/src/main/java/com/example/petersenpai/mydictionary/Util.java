package com.example.petersenpai.mydictionary;

import android.content.Context;
import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;



public class Util {

    private static List<Word> history_words;

    public static List<Word> getHistoryWords (Context context) {
        if (history_words == null) {
            try {
                history_words = Util.pull2xml(context.openFileInput("history_words.xml"));
            } catch (Exception e) {
                // if file does not exist, create an empty file
                Log.d("system.out", "file does not exist, create file");
                history_words = new ArrayList<>();
                try {
                    Util.createXML(history_words, context.openFileOutput("history_words.xml", Context.MODE_PRIVATE));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                e.printStackTrace();
            }
        }
        return history_words;
    }

    public static void clearHistoryWords () {
        history_words = new ArrayList<>();
    }
    public static void hideTranslation(){
        for(Word word : history_words){
            word.setShow();
        }
    }
    public static void addHistoryWord (Word word_to_add) {
        Boolean flag;
        if (history_words.size() == 0){
            flag = true;
        }else {flag = history_words.get(0).getShow();}

        for (Word word : history_words) {
            if (word.getSpell().equals(word_to_add.getSpell())) {
                history_words.remove(word);
                history_words.add(0, word);
                return;
            }
        }
        if (flag == false){
            word_to_add.setShow();
            history_words.add(0, word_to_add);
        }else {
            history_words.add(0, word_to_add);
        }
    }

    public static void SaveHistoryWordsToFile(Context context) {
        try {
            Util.createXML(history_words, context.openFileOutput("history_words.xml", Context.MODE_PRIVATE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get_response(String word){
        try {
            String api_key = "644299FD435E4FE1DAE27FC79933C462";
            String url = "http://dict-co.iciba.com/api/dictionary.php?w=" + word.toLowerCase().replaceAll("[^A-Za-z0-9]", "") + "&key=" + api_key;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            //print in String
            // System.out.println(response.toString());
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                    .parse(new InputSource(new StringReader(response.toString())));
            NodeList errNodes = doc.getElementsByTagName("dict");
            if (errNodes.getLength() > 0) {
                Element err = (Element)errNodes.item(0);
                System.out.println("词性: "+err.getElementsByTagName("pos").item(0).getTextContent());
                System.out.println("翻译: "+err.getElementsByTagName("acceptation").item(0).getTextContent());
                return err.getElementsByTagName("pos").item(0).getTextContent() +
                        err.getElementsByTagName("acceptation").item(0).getTextContent();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return "error";
    }

    private static List<Word> pull2xml(InputStream is) throws Exception {
        List<Word> list = null;
        Word word = null;
        //创建xmlPull解析器
        XmlPullParser parser = Xml.newPullParser();
        ///初始化xmlPull解析器
        parser.setInput(is, "utf-8");
        //读取文件的类型
        int type = parser.getEventType();
        //无限判断文件类型进行读取
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                //开始标签
                case XmlPullParser.START_TAG:
                    if ("word_list".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("word".equals(parser.getName())) {
                        word = new Word();
                    } else if ("spell".equals(parser.getName())) {
                        assert word != null;
                        word.setSpell(parser.nextText());
                    } else if ("translation".equals(parser.getName())) {
                        assert word != null;
                        word.setTranslation(parser.nextText());
                    }
                    break;
                //结束标签
                case XmlPullParser.END_TAG:
                    if ("word".equals(parser.getName())) {
                        assert list != null;
                        list.add(word);
                    }
                    break;
            }
            //继续往下读取标签类型
            type = parser.next();
        }
        return list;
    }

    private static void createXML(List<Word> word_list, OutputStream os) throws Exception {
        XmlSerializer serializer = Xml.newSerializer();
        serializer.setOutput(os, "utf-8");
        serializer.startDocument("utf-8", true);
        serializer.startTag(null, "word_list");
        for(Word word : word_list) {
            serializer.startTag(null, "word");

            serializer.startTag(null, "spell");
            serializer.text(word.getSpell());
            serializer.endTag(null, "spell");

            serializer.startTag(null, "translation");
            serializer.text(word.getTranslation());
            serializer.endTag(null, "translation");

            serializer.endTag(null, "word");
        }
        serializer.endTag(null, "word_list");
        serializer.endDocument();
        os.flush();
        os.close();
    }

}