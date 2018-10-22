package com.kuehne_nagel.model;

import com.kuehne_nagel.model.ranking.Ranking;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;

import static java.lang.String.valueOf;

public class XMLOutput implements Output {

    private final String fileName;
    private final XMLOutputFactory factory;

    public XMLOutput(String fileName) {
        this.fileName = fileName;
        factory = XMLOutputFactory.newInstance();
    }

    @Override
    public void write(List<Ranking> rankings) {
        try {
            XMLStreamWriter writer = factory.createXMLStreamWriter(
                    new FileWriter(fileName + ".xml"));
            writer.writeStartDocument();
            writer.writeStartElement("decathlon");
            writer.writeStartElement("rankings");
            Iterator<Ranking> rankingIterator = rankings.iterator();
            while (rankingIterator.hasNext()) {
                Ranking ranking = rankingIterator.next();
                writer.writeStartElement("ranking");
                writer.writeAttribute("position",ranking.getRankAsString());
                writer.writeAttribute("score",valueOf(ranking.getScore()));
                writer.writeCharacters(ranking.getContestant());
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }
}
