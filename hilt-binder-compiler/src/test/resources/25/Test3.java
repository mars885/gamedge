import com.paulrybitskyi.hiltbinder.BindType;

import javax.inject.Named;

@Named("three")
@BindType(
    contributesTo = BindType.Collection.SET,
    withQualifier = true
)
public class Test3 implements Testable {}