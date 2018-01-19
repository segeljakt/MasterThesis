################################################################################
#     File Name           :     makefile                                       #
#     Created By          :     Klas Segeljakt                                 #
#     Creation Date       :     [2017-04-18 15:37]                             #
#     Last Modified       :     [2017-09-23 14:40]                             #
#     Description         :     Makefile for pandoc.                           #
################################################################################
DOC=README
#-------------------------------------------------------------------------------
BIN_DIR=bin
DOC_DIR=doc
#-------------------------------------------------------------------------------
all: $(DOC)
##################################### DOC ######################################
DOCC=pandoc
#------------------------------------------------------------------------------
SRC_FMT=md
DST_FMT=pdf
BIB_FMT=bib
TEM_FMT=latex
#------------------------------------------------------------------------------
STL_PATH=/Users/Klas/Git/writing/styles/harvard-cite-them-right.csl
SRC_PATH=$(DOC_DIR)/$(DOC).$(SRC_FMT)
DST_PATH=$(BIN_DIR)/$(DOC).$(DST_FMT)
BIB_PATH=~/.vim/bibtex/library.bib
TEM_PATH=/Users/Klas/Git/writing/markdown-template.latex
LST_PATH=/Users/Klas/Git/writing/markdown-listings.latex
# Flags
DOC_FLAGS += --bibliography $(BIB_PATH)
DOC_FLAGS += --csl=$(STL_PATH)
DOC_FLAGS += -o $(DST_PATH)
DOC_FLAGS += --template $(TEM_PATH)
DOC_FLAGS += -N
DOC_FLAGS += -mathml
DOC_FLAGS += --listings
DOC_FLAGS += -H $(LST_PATH)
#-------------------------------------------------------------------------------
$(DOC):
	$(DOCC) $(SRC_PATH) $(DOC_FLAGS)
#################################### CLEAN #####################################
clean:
	rm -rf $(OBJ_DIR)/*
	rm -rf $(BIN_DIR)/*
