-- 添加外键约束
ALTER TABLE Reader
ADD CONSTRAINT FK_Reader_ReaderType 
FOREIGN KEY (ReaderTypeID) REFERENCES ReaderType(TypeID);

ALTER TABLE Book
ADD CONSTRAINT FK_Book_Publisher 
FOREIGN KEY (PublisherID) REFERENCES Publisher(PublisherID);

ALTER TABLE Book
ADD CONSTRAINT FK_Book_BookType 
FOREIGN KEY (BookTypeID) REFERENCES BookType(TypeID);

ALTER TABLE BorrowRecord
ADD CONSTRAINT FK_BorrowRecord_Reader 
FOREIGN KEY (ReaderID) REFERENCES Reader(ReaderID);

ALTER TABLE BorrowRecord
ADD CONSTRAINT FK_BorrowRecord_Book 
FOREIGN KEY (BookID) REFERENCES Book(BookID);

-- 创建索引以提高查询性能
CREATE INDEX IX_Book_Title ON Book(Title);
CREATE INDEX IX_Book_Author ON Book(Author);
CREATE INDEX IX_Reader_Name ON Reader(ReaderName);
CREATE INDEX IX_BorrowRecord_Status ON BorrowRecord(Status);
