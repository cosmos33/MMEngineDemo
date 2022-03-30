package com.immomo.xengine.demo.model;

public class GameConfig {
    //游戏显示语言
    //  "en" 英语
    //	"vi" 越南语
    //	"id" 印尼语
    //	"zh-cn" 简体中文
    //	"zh-tw" 繁体中文
    private String language = "zh-cn";

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
