package com.tmp;

import com.tmp.util.EncryptUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.RandomStringUtils;

public class DigestMain {

    public static void main(String [] args) {
        String data="";
        String spec="";
        String result = EncryptUtil.getSign(data, spec);
        System.out.println(result);
    }
}
