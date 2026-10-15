#if canImport(Testing)
import Testing
import Dunce

@Suite("Dunce Swift Export Tests")
struct DunceExportTests {
    @Test("Dunce swift module imported cleanly")
    func testSwiftModuleLoads() throws {
        #expect(Bool(true), "Dunce swift module imported cleanly")
    }
}
#elseif canImport(XCTest)
import XCTest
import Dunce

final class DunceExportTests: XCTestCase {
    func testSwiftModuleLoads() throws {
        XCTAssertTrue(true, "Dunce swift module imported cleanly")
    }
}
#endif
