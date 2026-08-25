import Testing
import Dunce

@Suite struct DunceExportTests {
    @Test func testSwiftModuleLoads() {
        #expect(Bool(true), "Dunce swift module imported cleanly")
    }
}
