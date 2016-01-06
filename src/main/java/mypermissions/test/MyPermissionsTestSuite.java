package mypermissions.test;

import metest.BaseSuite;
import mypermissions.test.command.CommandTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    CommandTest.class
})
public class MyPermissionsTestSuite extends BaseSuite {
}
