# Advancing Arabic Diacritization

Resources released with the paper
*Advancing Arabic Diacritization: Improved Datasets, Benchmarking, and State-of-the-Art Models*.

This repository provides all publicly shareable datasets and tools developed in the study, enabling researchers to build upon our results.

---

## Contents

### 1. Wikipedia Diacritized Corpus

* **Size:** \~5 million words
* **Source:** 32,835 Wikipedia articles (dump date 2024-02-20)
* **Description:** Each article is fully diacritized by our best-performing BiLSTM model (with a Word Error Rate of ~3% on WikiNews-2014).
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
@inproceedings{mohamed-mubarak-2025-advancing,
    title = "Advancing {A}rabic Diacritization: Improved Datasets, Benchmarking, and State-of-the-Art Models",
    author = "Mohamed, Abubakr  and
      Mubarak, Hamdy",
    editor = "Christodoulopoulos, Christos  and
      Chakraborty, Tanmoy  and
      Rose, Carolyn  and
      Peng, Violet",
    booktitle = "Proceedings of the 2025 Conference on Empirical Methods in Natural Language Processing",
    month = nov,
    year = "2025",
    address = "Suzhou, China",
    publisher = "Association for Computational Linguistics",
    url = "https://aclanthology.org/2025.emnlp-main.846/",
    doi = "10.18653/v1/2025.emnlp-main.846",
    pages = "16718--16730",
    ISBN = "979-8-89176-332-6",
    abstract = "Arabic diacritics, similar to short vowels in English, provide phonetic and grammatical information but are typically omitted in written Arabic, leading to ambiguity. Diacritization (aka diacritic restoration or vowelization) is essential for natural language processing. This paper advances Arabic diacritization through the following contributions: first, we propose a methodology to analyze and refine a large diacritized corpus to improve training quality. Second, we introduce WikiNews-2024, a multi-reference evaluation methodology with an updated version of the standard benchmark ``WikiNews-2014''. In addition, we explore various model architectures and propose a BiLSTM-based model that achieves state-of-the-art results with 3.12{\%} and 2.70{\%} WER on WikiNews-2014 and WikiNews-2024, respectively. Moreover, we develop a model that preserves user-specified diacritics while maintaining accuracy. Lastly, we demonstrate that augmenting training data enhances performance in low-resource settings."
}
```
