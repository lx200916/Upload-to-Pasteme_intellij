import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.ui.TitledSeparator;
import com.intellij.util.ui.UI;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class Setting implements SearchableConfigurable, Configurable.NoScroll {
    private JCheckBox IsCopyC = new JCheckBox();
    private JCheckBox IsOpenC = new JCheckBox();
    private JCheckBox IsFormatC = new JCheckBox();

    private JTextField DevToken = new JTextField();
    private JTextField PastePass = new JTextField();
    private JButton button3;
    private JLabel DefaultPass;
    private JPanel p;
    private PasteMeSettings pasteMeSettings = ServiceManager.getService(PasteMeSettings.class);
    ;

    @NotNull
    @Override
    public String getId() {
        return "plugins.PasteMe";
    }

    public Setting() {
        if (pasteMeSettings == null) {
            pasteMeSettings = ServiceManager.getService(PasteMeSettings.class);

        }
        IsCopyC = new JCheckBox("复制到剪贴板");
        IsOpenC = new JCheckBox("浏览器打开");
        DevToken = new JTextField();
        PastePass = new JTextField();
        IsFormatC = new JCheckBox("格式化代码");
        PastePass.setText(pasteMeSettings.defaultPass);
        DevToken.setText(pasteMeSettings.Token);
//        IsCopyC.setText("复制到剪贴板");
//        IsOpenC.setText("");
        IsCopyC.setSelected(pasteMeSettings.IsCopy);
        IsOpenC.setSelected(pasteMeSettings.IsOpenB);
        IsFormatC.setSelected(pasteMeSettings.IsFormat);
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "Upload to PasteMe";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return "help.PasteMe.configuration";
    }


    @Override
    public @Nullable JComponent createComponent() {
        JPanel p = UI.PanelFactory.grid().splitColumns().
                add(UI.PanelFactory.panel(DevToken).
                        withLabel("开发者Token").withComment("调用PasteMe.cn服务所需Token")).
                add(UI.PanelFactory.panel(PastePass).
                        withLabel("默认密码").withComment("将自动填充至Private Paste")).createPanel();
        p.setPreferredSize(new Dimension(1050, 120));
        p.setMaximumSize(new Dimension(1050, 120));

        JPanel x = new JPanel();
        x.setLayout(new BoxLayout(x,BoxLayout.Y_AXIS));
//       x.add(UI.PanelFactory.panel(new JLabel()).withTooltip("定义上传后的动作").createPanel(),BorderLayout.EAST);
        x.add(p);
        JPanel x3 = new JPanel();
        x3.setLayout(new BorderLayout());
        x3.add(new TitledSeparator("上传前动作"), BorderLayout.NORTH);
        x3.add(UI.PanelFactory.grid().add(UI.PanelFactory.panel(IsFormatC)).createPanel(), BorderLayout.CENTER);
        x.add(x3);
        JPanel x2 = new JPanel();
        x2.setLayout(new BorderLayout());
        x2.add(new TitledSeparator("上传后动作"), BorderLayout.NORTH);
        x2.add(UI.PanelFactory.grid().add(UI.PanelFactory.panel(IsCopyC)).add(UI.PanelFactory.panel(IsOpenC).withComment("调用默认浏览器打开PasteMe.cn,不适用于阅后即焚Paste").moveCommentRight()).createPanel(), BorderLayout.CENTER);
        x.add(x2);

        this.p = p;
        return x;
    }

    @Override
    public boolean isModified() {
        return (!DevToken.getText().equals(pasteMeSettings.Token) || !PastePass.getText().equals(pasteMeSettings.defaultPass) || IsOpenC.isSelected() != pasteMeSettings.IsOpenB || IsCopyC.isSelected() != pasteMeSettings.IsCopy||IsFormatC.isSelected()!=pasteMeSettings.IsFormat);
    }

    @Override
    public void apply() {
//        pasteMeSettings.loadState(new PasteMeSettings(PastePass.getText(),DevToken.getText(),IsCopyC.isSelected(),IsOpenC.isSelected()));
        pasteMeSettings.Token = DevToken.getText();
        pasteMeSettings.defaultPass = PastePass.getText();
        pasteMeSettings.IsOpenB = IsOpenC.isSelected();
        pasteMeSettings.IsCopy = IsCopyC.isSelected();
        pasteMeSettings.IsFormat = IsFormatC.isSelected();
    }

    @Override
    public void reset() {
//        if (pa != null) {
//            configuration.refresh(settings.getCodeTemplates());
//        }

        PastePass.setText(pasteMeSettings.defaultPass);
        DevToken.setText(pasteMeSettings.Token);
        IsCopyC.setSelected(pasteMeSettings.IsCopy);
        IsOpenC.setSelected(pasteMeSettings.IsOpenB);
        IsFormatC.setSelected(pasteMeSettings.IsFormat);
    }
}
