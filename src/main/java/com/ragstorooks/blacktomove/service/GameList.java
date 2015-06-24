package com.ragstorooks.blacktomove.service;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class GameList {
    public List<String> games;

    public GameList(List<String> games) {
        this.games = games;
    }

    public GameList() {}
}
