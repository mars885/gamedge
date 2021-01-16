import com.paulrybitskyi.hiltbinder.BindType;

import javax.inject.Named;

@Named("two")
@BindType(
    contributesTo = BindType.Collection.SET,
    withQualifier = true
)
public class Test2 implements Testable {}