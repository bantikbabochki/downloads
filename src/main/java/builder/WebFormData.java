package builder;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class WebFormData {
    private String textInput;
    private String password;
    private String textArea;
    private String disabledInput;
    private String readOnlyInput;
    private String selectDropdownValue;
    private String dataListDropdownValue;
    private String filePath;
    private Boolean checkedCheckbox;
    private Boolean defaultCheckbox;
    private String radioValue;
    private String colourPicker;
    private String datePicker;
    private String rangeValue;
}
