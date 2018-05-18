package switcher.pkg2.pkg0;


public class Model {
    public String name, path;
    public int start=0000, end=0000;
    
    Model(String name, String path, int start, int end)
    {
        this.path = path;
        this.name = name;
        this.start = start;
        this.end = end;
    }
    
    public String getFilePath()
    {
        return path;
    }
    
    public String getFileName()
    {
        return name;
    }
    
    public int getStartTime()
    {
        return start;
    }
    
    public int getEndTime()
    {
        return end;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }
    
    
}
