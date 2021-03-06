import com.github.lx200916.uploadtopastemeintellij.listeners.OnPrivateDialogListener;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.fields.ExtendableTextField;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Pattern;

public class PrivatePasteDialog extends JDialog {
    private final String lang;
    private JButton cancelButton;
    private JButton OKButton;
    private JTextField PasteID;
    private JPasswordField PastePass;
    private JButton button3;
    private JComboBox PasteLang;
    private JLabel languageLabel;
    private JTextArea PasteContent;
    private JLabel contentLabel;
    private JPanel contentPanel;
    private JCheckBox burnAfterRead;

    public PrivatePasteDialog(String pasteLang, final String pasteContent, String password,final OnPrivateDialogListener listener) {
        setContentPane(contentPanel);
        setModal(true);
        setTitle("Create Private Paste");


        setSize(600, 600);
        PastePass.setText(password);
        setPasteContent(pasteContent);
        setPasteLang(pasteLang);
        System.out.println(PastePass.getEchoChar());
        lang = pasteLang;
        PasteID.setText("You can specify PasteID only in a Burn After Read Paste.");
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                listener.onCancelClicked(PrivatePasteDialog.this);

            }
        });


        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onCancelClicked(PrivatePasteDialog.this);
            }
        });

        cancelButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.onCancelClicked(PrivatePasteDialog.this);
            }
        });
        OKButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (burnAfterRead.isSelected() && getPasteID().getText().length() != 0) {
                    if (Pattern.matches("^[0-9a-z]{3,8}$", getPasteID().getText())) {
                        listener.onOkClicked(PrivatePasteDialog.this);
                    } else {
                        Messages.showMessageDialog("Sorry, PasteID should be 8-16 long with digits and letters.", "Error", Messages.getErrorIcon());
                        return;
                    }

                } else {
                    listener.onOkClicked(PrivatePasteDialog.this);

                }
            }
        });
        burnAfterRead.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JCheckBox checkBox = (JCheckBox) e.getSource();
                Boolean state = checkBox.getModel().isSelected();
                getPasteID().setEnabled(state);
                getPasteID().setText("");


            }
        });
        button3.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
//                super.mousePressed(e);
                PastePass.setEchoChar((char)0);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
//                super.mouseReleased(e);
                PastePass.setEchoChar('•');

            }

//            @Override
//            public void mouseClicked(MouseEvent e) {
////                super.mouseClicked(e);
//                System.out.println(PastePass.getEchoChar());
//                if(PastePass.getEchoChar()=='*'){
//                    PastePass.setEchoChar((char)0);
//                }else {
//                    PastePass.setEchoChar('*');
//
//                }
//            }
        });
    }


    public String getPasteContent() {
        return PasteContent.getText();
    }

    public void setPasteContent(String pasteContent) {
//        PasteContent = pasteContent;
        PasteContent.setText(pasteContent);

    }

    public Boolean getBurnAfterRead() {
        return burnAfterRead.isSelected();
    }

    public void setBurnAfterRead(Boolean burn) {
//        this.burnAfterRead = burnAfterRead;
        burnAfterRead.setSelected(burn);
    }

    public JTextField getPasteID() {
        return PasteID;
    }

//    public void setPasteID(String pasteID) {
//        setPasteID(pasteID);
//    }

    public String getPasteLang() {

        if (PasteLang.getSelectedItem() == null) {
            return lang;
        } else {
            System.out.println(PasteLang.getSelectedItem().toString());
//            Messages.showMessageDialog(PasteLang.getSelectedItem().toString(), "Error", Messages.getErrorIcon());

            return PasteLang.getSelectedItem().toString();

        }
    }

    public void setPasteLang(String pasteLang) {
//        PasteLang = pasteLang;
        PasteLang.getModel().setSelectedItem(pasteLang);
    }

    public String getPastePass() {
        return new String(PastePass.getPassword());
    }

    public void invalidPasteid() {
        com.intellij.openapi.ui.Messages.showMessageDialog("Oops..PasteID has been Taken..", "PasteID Taken", new ImageIcon("/icon/ohno.gif"));

    }
}
