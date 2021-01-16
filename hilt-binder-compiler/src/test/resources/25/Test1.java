import com.paulrybitskyi.hiltbinder.BindType;

import javax.inject.Named;

@Named("one")
@BindType(
    contributesTo = BindType.Collection.SET,
    withQualifier = true
)
public class Test1 implements Testable {}