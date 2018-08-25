package com.designptn.decorator;

//装饰器模式,起装饰的作用，就是在一个类上增加功能。
//如果通过继承来增加功能，在不修改代码的情况下，如果增加功能多的话，会使类的数量爆炸式增长，装饰器模式能很好地解决了这一点。
public class Decorator implements Clothes {

    protected Clothes clothes;

    public Decorator(Clothes clothes) {
        this.clothes = clothes;
    }

    public void makeClothes() {
        clothes.makeClothes();
    }
}