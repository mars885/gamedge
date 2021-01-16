import com.paulrybitskyi.hiltbinder.BindType;

import javax.inject.Named;

@Named("test")
@BindType(withQualifier = true)
public class Test implements Testable {}