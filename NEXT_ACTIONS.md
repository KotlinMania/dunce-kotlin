# Immediate Actions - High-Value Files

Based on AST analysis, here are the concrete next steps.

## Summary

- **Files Present:** 1/1 (100.0%)
- **Function parity:** 15/15 matched (target 19) — 100.0%
- **Class/type parity:** 0/0 matched (target 1) — N/A
- **Combined symbol parity:** 15/15 matched (target 20) — 100.0%
- **Average inline-code cosine:** 0.52 (function body across 1 matched files)
- **Average documentation cosine:** 0.91 (doc text across 1 matched files)
- **Cheat-zeroed Files:** 0
- **Critical Issues:** 1 files with <0.60 function similarity

## Priority 1: Fix Incomplete High-Dependency Files

No incomplete high-dependency files detected.

## Priority 2: Port Missing High-Value Files

Critical missing files (>10 dependencies):

No missing high-value files detected.

## Detailed Work Items

Every matched file is listed below with function and type symbol parity.

### 1. lib

- **Target:** `dunce.Lib`
- **Similarity:** 0.52
- **Dependents:** 0
- **Priority Score:** 1504.8
- **Functions:** 15/15 matched (target 19)
- **Missing functions:** _none_
- **Types:** 0/0 matched (target 1)
- **Missing types:** _none_
- **Tests:** 7/7 matched

## Success Criteria

For each file to be considered "complete":
- **Similarity ≥ 0.85** (Excellent threshold)
- All public APIs ported
- All tests ported
- Documentation ported
- port-lint header present
