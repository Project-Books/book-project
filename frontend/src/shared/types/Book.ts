export type Book = {
  title: string;
  img: string;
  // Add img property once we have thumbnails
  author: {
    fullName: string;
  };
  predefinedShelf: {
    shelfName: string;
  };
  bookGenre: string[];
  rating: number;
};
