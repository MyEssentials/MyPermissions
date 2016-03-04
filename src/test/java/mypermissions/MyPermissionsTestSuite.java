package mypermissions;

import metest.api.BaseSuite;
import mypermissions.test.command.CommandTest;
import mypermissions.test.permissions.PermissionsTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        CommandTest.class,
        PermissionsTest.class
})
public class MyPermissionsTestSuite extends BaseSuite {


}
