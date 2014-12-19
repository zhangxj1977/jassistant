/*
 * blanco Framework
 * Copyright (C) 2004-2006 WATANABE Yoshinori
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 */
package org.jas.db.format.valueobject;

/**
 * BlancoSqlFormatter: SQL整形ツール. SQL文を決められたルールに従い整形します。
 * 
 * SQL文として正しいことが前提条件です。
 * http://homepage2.nifty.com/igat/igapyon/diary/2005/ig050613.html <br>
 * 
 * このクラスはトークンをあらわします。
 * 
 * @author WATANABE Yoshinori (a-san) : original version at 2005.07.04.
 * @author IGA Tosiki : marge into blanc Framework at 2005.07.04
 */
public class BlancoSqlToken extends AbstractBlancoSqlToken {
    /**
     * SQLトークンのインスタンスを作成します。
     * 
     * @param argType
     * @param argString
     * @param argPos
     */
    public BlancoSqlToken(int argType, String argString, int argPos) {
        setType(argType);
        setString(argString);
        setPos(argPos);
    }

    /**
     * SQLトークンのインスタンスを作成します。
     * 
     * @param argType
     * @param argString
     */
    public BlancoSqlToken(int argType, String argString) {
        this(argType, argString, -1);
    }
}
