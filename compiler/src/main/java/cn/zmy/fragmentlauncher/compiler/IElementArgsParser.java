package cn.zmy.fragmentlauncher.compiler;

import java.util.List;

import javax.lang.model.element.Element;

public interface IElementArgsParser
{
    List<ArgModel> parse(Element element);
}
