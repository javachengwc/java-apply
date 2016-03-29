package com.other.calendar;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public class CalendarFrame extends JFrame implements ActionListener
{

    JLabel labelDay[]=new JLabel[42];

    JTextField  text=new JTextField(10);

    JButton titleName[]=new JButton[7];

    JButton button = new JButton();

    String name[]={"日","一","二","三", "四","五","六"};

    JButton nextMonth,previousMonth;

    CalendarBean calendar;

    JLabel showMessage=new JLabel("",JLabel.CENTER);

    JLabel lbl1 = new JLabel("请输入年份：");

    JLabel lbl2=new JLabel("      ");

    int year;

    int month;

    public CalendarFrame()
    {

        JPanel pCenter=new JPanel();

        //将pCenter的布局设置为7行7列的GridLayout 布局。
        pCenter.setLayout(new GridLayout(7,7));

        //pCenter添加组件titleName[i]
        for(int i=0;i<7;i++)
        {
            titleName[i]=new JButton(name[i]);
            pCenter.add(titleName[i]);
        }

        //pCenter添加组件labelDay[i]
        for(int i=0;i<42;i++)
        {
            labelDay[i]=new JLabel("",JLabel.CENTER);
            pCenter.add(labelDay[i]);
        }

        text.addActionListener(this);
        Calendar ca = Calendar.getInstance();
        year =ca.get(Calendar.YEAR);
        month=ca.get(Calendar.MONTH)+1;
        calendar=new CalendarBean();
        calendar.setYear(year);
        calendar.setMonth(month);
        String day[]=calendar.getCalendar();

        for(int i=0;i<42;i++)
        {
            labelDay[i].setText(day[i]);
        }
        nextMonth=new JButton("下月");
        previousMonth=new JButton("上月");
        button=new JButton("确定");

        //注册监听器
        nextMonth.addActionListener(this);
        previousMonth.addActionListener(this);
        button.addActionListener(this);

        JPanel pNorth=new JPanel(),pSouth=new JPanel();

        pNorth.add(showMessage);

        pNorth.add(lbl2);

        pNorth.add(previousMonth);

        pNorth.add(nextMonth);

        pSouth.add(lbl1);

        pSouth.add(text);

        pSouth.add(button);

        showMessage.setText("日历："+calendar.getYear()+"年"+ calendar.getMonth()+"月" );

        ScrollPane scrollPane=new ScrollPane();

        scrollPane.add(pCenter);

        add(scrollPane,BorderLayout.CENTER);// 窗口添加scrollPane在中心区域

        add(pNorth,BorderLayout.NORTH);// 窗口添加pNorth 在北面区域

        add(pSouth,BorderLayout.SOUTH);// 窗口添加pSouth 在南区域。

    }

    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==nextMonth)
        {
            month=month+1;
            if(month>12) {
               month = 1;
            }
            calendar.setMonth(month);
            String day[]=calendar.getCalendar();
            for(int i=0;i<42;i++)
            {
                labelDay[i].setText(day[i]);
            }
        }
        else if(e.getSource()==previousMonth)
        {
            month=month-1;
            if(month<1) {
                month = 12;
            }
            calendar.setMonth(month);
            String day[]=calendar.getCalendar();
            for(int i=0;i<42;i++)
            {
                labelDay[i].setText(day[i]);
            }
        }
        else if(e.getSource()==button)
        {
            month=month+1;
            if(month>12) {
                month = 1;
            }
            calendar.setYear(Integer.parseInt(text.getText()));
            String day[]=calendar.getCalendar();
            for(int i=0;i<42;i++)
            {
                labelDay[i].setText(day[i]);
            }
        }
        showMessage.setText("日历："+calendar.getYear()+"年"+calendar.getMonth()+"月" );
    }
}
