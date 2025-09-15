# Advancing Arabic Diacritization

Resources released with the paper
*Advancing Arabic Diacritization: Improved Datasets, Benchmarking, and State-of-the-Art Models*.

This repository provides all publicly shareable datasets and tools developed in the study, enabling researchers to build upon our results.

---

## Contents

### 1. Wikipedia Diacritized Corpus

* **Size:** \~5 million words
* **Source:** 32,835 Wikipedia articles (dump date 2024-02-20)
* **Description:** Each article is fully diacritized by our best-performing BiLSTM model.
* **Format:** JSONL (one article per line).

### 2. WikiNews Benchmarks

* **WikiNews-2014 (multi-reference):** The original benchmark enhanced with multi-reference annotations and consistency fixes.
* **WikiNews-2024:** A newly curated benchmark (\~10 k words) fully diacritized and multi-reference annotated.

### 3. Scoring Script

* Updated evaluation script that **accounts for multi-reference diacritization**, ensuring accurate WER (Word Error Rate) and DER (Diacritic Error Rate) measurements.
* Backwards-compatible: can still be run in single-reference mode.

---

## Getting Started

Clone the repository:

```bash
git clone https://github.com/Abubakr17/advancing-arabic-diacritization.git
cd advancing-arabic-diacritization
```

### Directory Structure

```
advancing-arabic-diacritization/
│
├─ Wikipedia_Diacritized_Corpus/           # Wikipedia corpus (~5M words)
├─ WikiNews_Benchmarks/     # Multi-reference WikiNews-2014 and new WikiNews-2024 dataset
└─ Evaluation/             # Multi-reference scoring script
```

---

## Usage

Evaluate a system output against a benchmark:

```bash
cd evaluation
java EvalDiac.java \
   --ref <PATH_TO_REFERENCE_FILE> \
   --sys <PATH_TO_YOUR_MODEL_OUTPUT>
```

The script reports both **WER** and **DER**, with or without multi-reference mode.

---

## Citation

If you use these datasets or scripts, please cite:

```
@inproceedings{advancing-arabic-diacritization,
  title     = {Advancing Arabic Diacritization: Improved Datasets, Benchmarking, and State-of-the-Art Models},
  author    = {Mohamed, Abubakr and Mubarak, Hamdy},
  booktitle = {Proceedings of the 2025 Conference on Empirical Methods in Natural Language Processing},
  year      = {2025}
}
```

(A final BibTeX entry will be added once the paper is published.)

