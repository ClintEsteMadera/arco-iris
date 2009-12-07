package commons.gui.swt.text;

public interface TextFilterContext {

    void insert(int index,String text);

    void remove(int index, int length);

    void replace(int index, int length,String text);

    void clear();

    public String getText();
    
    public int getTextLength();
    
    public int getSelectionStart();
    
    public void setSelectionStart(int s);
    
    public int getSelectionLength();
    
    public void setSelectionLength(int l);
}
