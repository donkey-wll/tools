package com.zbba.tool.config;

import com.alibaba.druid.sql.SQLUtils;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.fusesource.jansi.Ansi;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.fusesource.jansi.Ansi.Color;


public class P6SpyLogger implements MessageFormattingStrategy {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql) {
        //date类型显示转换
        sql = sql.replaceAll("\'TO_DATE\\(", "TO_DATE('")
                .replaceAll(",YYYY-MM-DD HH24:MI:SS\\)\'", "','YYYY-MM-DD HH24:MI:SS')");

        //替换换行符及多于的空格
        sql = sql.replaceAll("\\n", "").replaceAll("\\s+", " ");
        //未格式化的sql语句
        String unFormatSql = sql;
        sql = SQLUtils.formatOracle(sql);

        //in('1','2')中的多行参数替换为一行显示
        sql = sql.replaceAll(", \\n\\t+", ", ");
        if (!sql.trim().equals("")) {
            Color elapsedColor = Color.CYAN;
            if (elapsed > 2000L) {
                //超过2s显示红色
                elapsedColor = Color.RED;
            }

            return "\n-----------------------------------------------------------------------------------------------------------\n"
                    + Ansi.ansi().fg(Color.YELLOW).a(String.format("formatedSql:\n%s\n\n", sql)).reset()
                    + Ansi.ansi().fg(Color.BLUE).a(String.format("unFormatedSql:\n %s\n", unFormatSql)).reset()
                    + format.format(new Date())
                    + " | took " + Ansi.ansi().fg(elapsedColor).a(elapsed + "ms").reset()
                    + " | " + category
                    + " | connection " + connectionId
                    + "\n-----------------------------------------------------------------------------------------------------------";
        }
        return "";
    }
}
