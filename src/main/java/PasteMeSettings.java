import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "PasteMeSettings", storages = {@Storage("$APP_CONFIG$/PasteMe-settings.xml")})
public class PasteMeSettings implements PersistentStateComponent<PasteMeSettings> {
public String  defaultPass;
public String Token;
public Boolean IsCopy;
public Boolean IsOpenB;
    public Boolean IsFormat=false;

    public PasteMeSettings() {
    }



    @Override
    public @Nullable PasteMeSettings getState() {
if (IsCopy==null){
    defaultPass="";
    Token="";
    IsCopy=false;
    IsOpenB=false;

}
        return this;
    }

    @Override
    public void noStateLoaded() {
        defaultPass="";
        Token="";
        IsCopy=false;
        IsOpenB=false;
        IsFormat=false;
    }

    @Override
    public void loadState(@NotNull PasteMeSettings state) {
        XmlSerializerUtil.copyBean(state, this);

    }

}
